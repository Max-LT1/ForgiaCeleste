package Control;

import DAO.DBConnection;
import DAO.DaoComposizione;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Client;
import model.Composizione;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/AggiungiCarrello")
public class Serv_AggiungiCarrello extends HttpServlet {

    private static final long serialVersionUID = 6L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        int productId = 0;
        int quantita = 0;

        try {
            JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
            productId = json.get("idProdotto").getAsInt();
            quantita = json.get("quantita").getAsInt();
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Dati JSON inviati non validi o mancanti.\"}");
            return;
        }

        if (quantita <= 0 || quantita > 99) {
            response.getWriter().write("{\"success\": false, \"message\": \"Quantità non valida\"}");
            return;
        }

        HttpSession session = request.getSession();
        Client client = (Client) session.getAttribute("cliente");

        if (client != null) {
            // 🟢 UTENTE LOGGATO: Aggiorna Sessione + Persistenza Database
            List<Composizione> carrello = (List<Composizione>) session.getAttribute("carrello");
            if (carrello == null) {
                carrello = new ArrayList<>();
            }

            boolean productExists = false;
            for (Composizione composizione : carrello) {
                if (composizione.getIdProdotto() == productId) {
                    productExists = true;
                    composizione.setQuantita_prodotto(quantita);
                    break;
                }
            }

            if (!productExists) {
                Composizione newComposizione = new Composizione();
                newComposizione.setIdProdotto(productId);
                newComposizione.setQuantita_prodotto(quantita);
                newComposizione.setEmail(client.getEmail());
                newComposizione.setUsername(client.getUsername());
                carrello.add(newComposizione);
            }

            session.setAttribute("carrello", carrello);

            // Persistenza Database usando i metodi supportati dal DaoComposizione
            try {
                DaoComposizione dao = new DaoComposizione(DBConnection.getDataSource());
                if (productExists) {
                    dao.updateQuantitaProdotto(client.getUsername(), client.getEmail(), productId, quantita);
                } else {
                    dao.doSave(client.getUsername(), client.getEmail(), productId, quantita);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            // 🟡 OSPITE: Persistenza solo su Sessione
            List<Composizione> carrelloNoLog = (List<Composizione>) session.getAttribute("carrelloNoLog");
            if (carrelloNoLog == null) {
                carrelloNoLog = new ArrayList<>();
            }

            boolean productExists = false;
            for (Composizione composizione : carrelloNoLog) {
                if (composizione.getIdProdotto() == productId) {
                    productExists = true;
                    composizione.setQuantita_prodotto(quantita);
                    break;
                }
            }

            if (!productExists) {
                Composizione newComposizione = new Composizione();
                newComposizione.setIdProdotto(productId);
                newComposizione.setQuantita_prodotto(quantita);
                carrelloNoLog.add(newComposizione);
            }

            session.setAttribute("carrelloNoLog", carrelloNoLog);
        }

        response.getWriter().write("{\"success\": true}");
    }
}
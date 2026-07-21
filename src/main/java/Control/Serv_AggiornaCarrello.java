package Control;

import DAO.DBConnection;
import DAO.DaoComposizione;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Client;
import model.Composizione;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/AggiornaCarrello")
public class Serv_AggiornaCarrello extends HttpServlet {
    private static final long serialVersionUID = 5162781596625596474L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String requestBody = request.getReader().lines().collect(Collectors.joining());
        JsonObject json = new Gson().fromJson(requestBody, JsonObject.class);

        int prodottoId = 0;
        if (json.has("idProdotto")) {
            prodottoId = json.get("idProdotto").getAsInt();
        } else if (json.has("prodottoId")) {
            prodottoId = json.get("prodottoId").getAsInt();
        }

        int quantita = json.get("quantita").getAsInt();

        if (quantita < 1 || quantita > 99) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Quantità non valida\"}");
            return;
        }

        HttpSession session = request.getSession();
        Client client = (Client) session.getAttribute("cliente");

        if (client == null) {
            // 🟡 OSPITE: Aggiorna la lista "carrelloNoLog"
            List<Composizione> carrelloNoLog = (List<Composizione>) session.getAttribute("carrelloNoLog");
            if (carrelloNoLog != null) {
                for (Composizione item : carrelloNoLog) {
                    if (item.getIdProdotto() == prodottoId) {
                        item.setQuantita_prodotto(quantita);
                        break;
                    }
                }
                session.setAttribute("carrelloNoLog", carrelloNoLog);
            }
        } else {
            // 🟢 UTENTE LOGGATO: Aggiorna la lista "carrello" in Sessione + DATABASE
            List<Composizione> carrello = (List<Composizione>) session.getAttribute("carrello");
            if (carrello != null) {
                for (Composizione item : carrello) {
                    if (item.getIdProdotto() == prodottoId) {
                        item.setQuantita_prodotto(quantita);
                        break;
                    }
                }
                session.setAttribute("carrello", carrello);
            }

            // Aggiorna DB
            try {
                DaoComposizione dao = new DaoComposizione(DBConnection.getDataSource());
                dao.updateQuantitaProdotto(client.getUsername(), client.getEmail(), prodottoId, quantita);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("success", true);
        response.getWriter().write(new Gson().toJson(responseJson));
    }
}
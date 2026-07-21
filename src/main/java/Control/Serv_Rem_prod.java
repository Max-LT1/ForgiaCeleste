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

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/RimuoviDalCarrello")
public class Serv_Rem_prod extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private DaoComposizione composizioneDAO;

    @Override
    public void init() {
        DataSource dataSource = DBConnection.getDataSource();
        composizioneDAO = new DaoComposizione(dataSource);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 1. Legge il JSON inviato da carrello.js
        String requestBody = request.getReader().lines().collect(Collectors.joining());
        JsonObject json = new Gson().fromJson(requestBody, JsonObject.class);

        String azione = (json != null && json.has("azione")) ? json.get("azione").getAsString() : "rimuovi";

        HttpSession session = request.getSession();
        Client cliente = (Client) session.getAttribute("cliente");

        int totaleArticoliRimanenti = 0;
        boolean successo = true;

        if ("svuota".equalsIgnoreCase(azione)) {
            // ==================== AZIONE: SVUOTA CARRELLO ====================
            if (cliente == null) {
                // Ospite
                List<Composizione> carrelloNoLog = (List<Composizione>) session.getAttribute("carrelloNoLog");
                if (carrelloNoLog != null) {
                    carrelloNoLog.clear();
                    session.setAttribute("carrelloNoLog", carrelloNoLog);
                }
            } else {
                // Loggato
                List<Composizione> carrello = (List<Composizione>) session.getAttribute("carrello");
                if (carrello != null) {
                    carrello.clear();
                    session.setAttribute("carrello", carrello);
                }
                try {
                    // Svuota DB se hai un metodo dedicato (es. clearComposizioniByCliente)
                    // composizioneDAO.clearComposizioniByCliente(cliente.getUsername(), cliente.getEmail());
                } catch (Exception e) {
                    e.printStackTrace();
                    successo = false;
                }
            }

        } else {
            // ==================== AZIONE: RIMUOVI SINGOLO PRODOTTO ====================
            int idProdotto = 0;
            if (json != null) {
                if (json.has("idProdotto")) {
                    idProdotto = json.get("idProdotto").getAsInt();
                } else if (json.has("prodottoId")) {
                    idProdotto = json.get("prodottoId").getAsInt();
                }
            }

            if (idProdotto <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\":false,\"errore\":\"ID Prodotto non valido\"}");
                return;
            }

            final int idDaRimuovere = idProdotto;

            if (cliente == null) {
                // Ospite
                List<Composizione> carrelloNoLog = (List<Composizione>) session.getAttribute("carrelloNoLog");
                if (carrelloNoLog != null) {
                    carrelloNoLog.removeIf(comp -> comp.getIdProdotto() == idDaRimuovere);
                    session.setAttribute("carrelloNoLog", carrelloNoLog);

                    for (Composizione comp : carrelloNoLog) {
                        totaleArticoliRimanenti += comp.getQuantita_prodotto();
                    }
                }
            } else {
                // Loggato
                List<Composizione> carrello = (List<Composizione>) session.getAttribute("carrello");
                if (carrello != null) {
                    carrello.removeIf(comp -> comp.getIdProdotto() == idDaRimuovere);
                    session.setAttribute("carrello", carrello);

                    for (Composizione comp : carrello) {
                        totaleArticoliRimanenti += comp.getQuantita_prodotto();
                    }
                }

                try {
                    composizioneDAO.removeComposizione(cliente.getUsername(), cliente.getEmail(), idProdotto);
                } catch (SQLException e) {
                    e.printStackTrace();
                    successo = false;
                }
            }
        }

        // 2. Risposta JSON finale per il JavaScript
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("success", successo);
        responseJson.addProperty("numeroArticoli", totaleArticoliRimanenti);

        response.getWriter().write(new Gson().toJson(responseJson));
    }
}
package Control;


import DAO.ClienteDAO;
import DAO.DBConnection;
import com.password4j.Password;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Client;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@WebServlet("/RegistrazioneServ")
public class Serv_register extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ClienteDAO clienteDAO;
    private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Client client = new Client();
        client.setUsername(request.getParameter("username"));
        client.setPassword(Password.hash(request.getParameter("password")).addRandomSalt().withArgon2().getResult());
        client.setEmail(request.getParameter("email"));
        client.setRuolo_cliente("cliente");
        client.setNome(request.getParameter("nome"));
        client.setCognome(request.getParameter("cognome"));
        client.setIndirizzo(request.getParameter("indirizzo"));
        client.setCitta(request.getParameter("citta"));
        client.setProvincia(request.getParameter("provincia"));
        client.setCap(request.getParameter("cap"));

        if (!validaTesto( client.getNome(), 2) ||
                !validaTesto(client.getCognome(), 2) ||
                !validaUsername(client.getUsername()) ||
                !validaEmail(client.getEmail()) ||
                !validaPassword(client.getPassword()) ||
                !validaTesto(client.getIndirizzo(), 5) ||
                !validaTesto(client.getCitta(), 2) ||
                !validaProvincia(client.getProvincia()) ||
                !validaCap(client.getCap())) {

            request.setAttribute("errorMessage", "Correggi i campi evidenziati prima di registrarti.");
            request.getRequestDispatcher("/log-sign.jsp").forward(request, response);
            return;
        }


        try {
            clienteDAO.addCliente(client);
            request.getRequestDispatcher("log-sign.jsp").forward(request, response);
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                // Errore utente già esistente
                String errorMessage = "username o email già esistono";
                request.setAttribute("errorMessage", errorMessage);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                request.getRequestDispatcher("/log-sign.jsp").forward(request, response);
            } else {
                // Errore generico
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Problema durante la registrazione.");
            }
        }

    }

    @Override
    public void init() throws ServletException {
        dataSource = DBConnection.getDataSource();
        clienteDAO = new ClienteDAO(dataSource);
    }

    public boolean validaTesto(String valore, int minLength) {
        if (valore == null || valore.trim().length() < minLength) return false;
        return valore.matches("^[A-Za-zÀ-ÖØ-öø-ÿ0-9' -]+$");
    }

    public boolean validaUsername(String username) {
        if (username == null) return false;
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    public boolean validaEmail(String email) {
        if (email == null) return false;
        return email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)+$");
    }

    public boolean validaPassword(String password) {
        if (password == null) return false;

        return password.length() >= 8 &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*(),.?\":{}|<>\\-_+=/\\\\\\[\\];'].*");
    }

    public boolean validaConfermaPassword(String password, String conferma) {
        if (conferma == null || conferma.isEmpty()) return false;
        return password.equals(conferma);
    }

    public boolean validaProvincia(String provincia) {
        if (provincia == null) return false;
        return provincia.toUpperCase().matches("^[A-Z]{2}$");
    }

    public boolean validaCap(String cap) {
        if (cap == null) return false;
        return cap.matches("^\\d{5}$");
    }


}

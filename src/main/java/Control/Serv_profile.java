package Control;


import DAO.ClienteDAO;
import DAO.DBConnection;
import com.mysql.cj.Session;
import com.password4j.Password;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Client;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet({"/Profilo", "/modificaUtente"})
public class Serv_profile extends HttpServlet {
    private static final long serialVersionUID = 5L;
    private ClienteDAO clienteDAO;
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle GET requests to the /register URL
        // Display an error message or redirect to an appropriate page
        String errorMessage = "HTTP GET Non è supportato";
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("error.jsp").forward(request, response);

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String servletPath = request.getServletPath();
        try {
            modificaUtente(request, response, session);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void modificaUtente(HttpServletRequest req, HttpServletResponse res, HttpSession session)
            throws ServletException, IOException, SQLException {
        String originalUsername = req.getParameter("originalUsername");
        String nome = req.getParameter("nome");
        String cognome = req.getParameter("cognome");
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String indirizzo = req.getParameter("indirizzo");
        String citta = req.getParameter("citta");
        String provincia = req.getParameter("provincia");
        String Ogpsw = req.getParameter("currentPassword");
        String newpsw = req.getParameter("newPassword");
        String confermapsw = req.getParameter("confirmNewPassword");
        if(!validaTesto(nome, 2) ||
                !validaTesto(cognome, 2) ||
                !validaUsername(username) ||
                !validaEmail(email) ||
                !validaTesto(indirizzo, 5) ||
                !validaTesto(citta, 2) ||
                !validaProvincia(provincia)
        ){

        }
        Client cliente = clienteDAO.getClienteByUsername(originalUsername);
        if(Password.check(Ogpsw, cliente.getPassword()).withArgon2()){
            cliente.setUsername(username);
            cliente.setEmail(email);
            cliente.setNome(nome);
            cliente.setCognome(cognome);
            cliente.setIndirizzo(indirizzo);
            cliente.setCitta(citta);
            cliente.setProvincia(provincia);
            if(!(newpsw.isEmpty()) && newpsw.equals(confermapsw)){
                cliente.setPassword(Password.hash(newpsw).addRandomSalt().withArgon2().getResult());
            }
            try {
                clienteDAO.updateCliente(cliente, originalUsername);
                session.setAttribute("cliente", cliente);
                req.getRequestDispatcher("HomePage").forward(req, res);
            } catch (SQLException e) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
        }else {
            res.sendRedirect("user-area.jsp");
        }


    }

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

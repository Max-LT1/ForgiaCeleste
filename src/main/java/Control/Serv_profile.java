package Control;


import DAO.ClienteDAO;
import DAO.DBConnection;
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

        switch(servletPath){
            case "/modificaUtente":
                try {
                    modificaUtente(request, response);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/Profilo":
                try {
                    Profile(request, response);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }


    public void Profile(HttpServletRequest req, HttpServletResponse res)
            throws SQLException, ServletException, IOException{
        String nome = req.getParameter("nome");
        String cognome = req.getParameter("cognome");
        String indirizzo = req.getParameter("indirizzo");
        String citta = req.getParameter("citta");
        String provincia = req.getParameter("provincia");
        String cap = req.getParameter("cap");

        if (!nome.matches("^[a-zA-Z]{1,50}$")) {
            String errorMessage = "Invalid nome (1-50 characters)";
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMessage);
            return;
        }
        if (!cognome.matches("^[a-zA-Z]{1,50}$")) {
            String errorMessage = "Invalid cognome (1-50 characters)";
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMessage);
            return;
        }

        // Validate indirizzo
        if (!indirizzo.matches("^[a-zA-Z0-9 ]{1,100}$")) {
            String errorMessage = "Invalid indirizzo (1-100 characters)";
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMessage);
            return;
        }

        // Validate citta
        if (!citta.matches("^[a-zA-Z]{1,50}$") || !provincia.matches("^[a-zA-Z]{1,50}$")) {
            String errorMessage = "Invalid indirizzo (1-50 characters)";
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMessage);
            return;
        }

        // Get the cliente ID from the session
        HttpSession session = req.getSession();
        Client cliente = ((Client) session.getAttribute("cliente"));

        if (cliente == null) {
            // Cliente is not authenticated, redirect to login page or show an error message
            res.sendRedirect("login.jsp");
            return;
        }

        // Create a new Cliente object

        cliente.setUsername(cliente.getUsername());
        cliente.setEmail(cliente.getEmail());
        cliente.setNome(nome);
        cliente.setCognome(cognome);
        cliente.setIndirizzo(indirizzo);
        cliente.setCitta(citta);
        cliente.setCap(cap);
        cliente.setProvincia(provincia);

        try {
            // Update the cliente details in the database
            clienteDAO.updateCliente(cliente);
            // Update the cliente object in the session
            session.setAttribute("cliente", cliente);
            // Redirect to the profile page with a success message
            res.sendRedirect("ProfiloUtente.jsp");
        } catch (SQLException e) {
            // Redirect to the profile page with an error message
            res.sendRedirect("ProfiloUtente.jsp");
        }
    }


    public void modificaUtente(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException, SQLException {
        String originalUsername = req.getParameter("OriginalUsername");
        String nome = req.getParameter("nome");
        String cognome = req.getParameter("cognome");
        String username = req.getParameter("username");
        String email = req.getParameter("currentEmail");
        String indirizzo = req.getParameter("indirizzo");
        String citta = req.getParameter("citta");
        String provincia = req.getParameter("provincia");
        String Ogpsw = req.getParameter("pswAttuale");
        String newpsw = req.getParameter("NuovaPassword");


        Client cliente = clienteDAO.getClienteByUsername(originalUsername);
        if(Password.check(Ogpsw, cliente.getPassword()).withArgon2()){
            cliente.setUsername(username);
            cliente.setEmail(email);
            cliente.setNome(nome);
            cliente.setCognome(cognome);
            cliente.setIndirizzo(indirizzo);
            cliente.setCitta(citta);
            cliente.setProvincia(provincia);
            if(newpsw != null){
                cliente.setPassword(newpsw);
            }
            try {
                clienteDAO.updateCliente(cliente);
                req.getRequestDispatcher("index.jsp").forward(req, res);
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
}

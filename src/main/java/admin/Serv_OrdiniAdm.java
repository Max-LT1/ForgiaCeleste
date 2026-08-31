package admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;



import DAO.ClienteDAO;
import DAO.DBConnection;
import DAO.DaoOrdine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Client;
import model.Ordine;

@WebServlet("/AdminOrdinePage")

public class Serv_OrdiniAdm extends HttpServlet {

    private static final long serialVersionUID = 16L;
    private DaoOrdine orderDAO;
    private ClienteDAO userDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Client cliente = (Client) session.getAttribute("cliente");
        if (cliente != null) {

            if (!(cliente.getRuolo_cliente().equals("admin"))) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not an admin.");
                return;
            }
        } else {
            String errorMessage = "You are not logged in, please login";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;

        }
        try {
            List<Client> userList = userDAO.getAllUsers();
            List<Ordine> orderList = orderDAO.getAllOrdini();
            request.setAttribute("clienteList", userList);
            request.setAttribute("ordineList", orderList);

            request.getRequestDispatcher("admin/AdminOrdiniPage.jsp").forward(request, response);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred while retrieving cliente orders." + e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Client cliente = (Client) session.getAttribute("cliente");
        if (cliente != null) {

            if (!(cliente.getRuolo_cliente().equals("admin"))) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not an admin.");
                return;
            }
        } else {
            String errorMessage = "You are not logged in, please login";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;

        }
        try {

            List<Client> userList = userDAO.getAllUsers();

            List<Ordine> orderList = null;
            String selectedUsername = request.getParameter("selectedUsername");
            String fromDate = request.getParameter("fromDate");
            String toDate = request.getParameter("toDate");
            if (fromDate != null && !fromDate.isEmpty() && toDate != null && !toDate.isEmpty()
                    && selectedUsername != null && !selectedUsername.isEmpty()) {

                java.sql.Date fromDateSql = java.sql.Date.valueOf(fromDate);
                java.sql.Date toDateSql = java.sql.Date.valueOf(toDate);
                orderList = orderDAO.getOrdini(fromDateSql, toDateSql, selectedUsername);
            } else if (fromDate != null && !fromDate.isEmpty() && toDate != null && !toDate.isEmpty()) {
                java.sql.Date fromDateSql = java.sql.Date.valueOf(fromDate);
                java.sql.Date toDateSql = java.sql.Date.valueOf(toDate);
                orderList = orderDAO.getOrdini(fromDateSql, toDateSql);
            } else if (selectedUsername != null && !selectedUsername.isEmpty()) {

                orderList = orderDAO.getOrdini(selectedUsername);
            } else {
                orderList = orderDAO.getAllOrdini();

            }

            request.setAttribute("clienteList", userList);
            request.setAttribute("ordineList", orderList);

            request.getRequestDispatcher("admin/AdminOrdiniPage.jsp").forward(request, response);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred while retrieving cliente orders." + e);
        }
    }

    @Override
    public void init() {
        userDAO = new ClienteDAO(DBConnection.getDataSource());
        orderDAO = new DaoOrdine(DBConnection.getDataSource());
    }

}
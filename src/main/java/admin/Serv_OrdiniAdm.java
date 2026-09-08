package admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import DAO.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Client;
import model.Composizione;
import model.Ordine;
import model.Prodotto;

@WebServlet("/AdminOrdinePage")

public class Serv_OrdiniAdm extends HttpServlet {

    private static final long serialVersionUID = 16L;
    private DaoOrdine orderDAO;
    private DaoProdotto produtDAO;
    private DaoComposizione compositionDAO;

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
            List<Ordine> orderList = orderDAO.getAllOrdini();
            List<Composizione> compositionList = new ArrayList<>();
            List<Prodotto> productList = new ArrayList<>();
            if(!orderList.isEmpty()){
                for(Ordine ordine : orderList) {
                    compositionList = compositionDAO.getComposizioniByUsernameAndEmail(ordine.getUsernameCliente(), ordine.getEmailCliente());
                }
            }
            if(!compositionList.isEmpty()){
                for(Composizione composizione : compositionList) {
                    productList.add(produtDAO.getProdottoById(composizione.getIdProdotto()));
                }
            }

            request.setAttribute("ordineList", orderList);
            request.setAttribute("productList", productList);
            request.setAttribute("compositionList", compositionList);

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
            List<Composizione> compositionList = new ArrayList<>();
            List<Prodotto> productList = new ArrayList<>();
            if(!orderList.isEmpty()){
                for(Ordine ordine : orderList) {
                    compositionList = compositionDAO.getComposizioniByUsernameAndEmail(ordine.getUsernameCliente(), ordine.getEmailCliente());
                }
            }
            if(!compositionList.isEmpty()){
                for(Composizione composizione : compositionList) {
                    productList.add(produtDAO.getProdottoById(composizione.getIdProdotto()));
                }
            }

            request.setAttribute("ordineList", orderList);
            request.setAttribute("productList", productList);
            request.setAttribute("compositionList", compositionList);

            request.getRequestDispatcher("admin/AdminOrdiniPage.jsp").forward(request, response);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred while retrieving cliente orders." + e);
        }
    }

    @Override
    public void init() {
        compositionDAO = new DaoComposizione(DBConnection.getDataSource());
        produtDAO = new DaoProdotto(DBConnection.getDataSource());
        orderDAO = new DaoOrdine(DBConnection.getDataSource());
    }

}
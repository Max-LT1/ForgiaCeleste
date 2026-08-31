package admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;


import DAO.DBConnection;
import DAO.DaoProdotto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Client;
import model.Prodotto;

@WebServlet("/addProdotto")
public class Serv_AddProduct extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private DaoProdotto prodottoDAO;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        double iva = Double.parseDouble(request.getParameter("iva"));
        String imagePath = request.getParameter("imagePath");
        boolean recommended = Boolean.parseBoolean(request.getParameter("recommended"));
        HttpSession session = request.getSession();

        Client cliente = (Client) session.getAttribute("cliente");
        String sessionToken = ((String) session.getAttribute("sessionToken"));
        String clientToken = (request.getParameter("clientToken"));
        if (cliente != null) {
            if (!sessionToken.equals(clientToken)) {
                String errorMessage = "You are not logged in, please login";
                request.setAttribute("errorMessage", errorMessage);
                request.getRequestDispatcher("Login.jsp").forward(request, response);
                return;
            }
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

        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid Price.");

            return;
        }

        Prodotto prodotto = new Prodotto();
        prodotto.setNomeProdotto(name);
        prodotto.setDescrizione(description);
        prodotto.setPrezzo(price);
        prodotto.setIva(iva);
        prodotto.setPath_immagine(imagePath);


        try {
            prodottoDAO.createProdotto(prodotto);
            response.sendRedirect("AdminCatalogPage");

        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error adding prodotto ");
            request.getRequestDispatcher("AdminCatalogPage").forward(request, response);

        }
    }

    @Override
    public void init() {
        prodottoDAO = new DaoProdotto(DBConnection.getDataSource());
    }

}
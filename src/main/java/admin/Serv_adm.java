package admin;

import DAO.DBConnection;
import DAO.DaoComposizione;
import DAO.DaoProdotto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Prodotto;
import model.Client;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet({ "/Update", "/Remove" })
public class Serv_adm extends HttpServlet {
    private static final long serialVersionUID = 15L;
    private DaoComposizione cartItemDAO;
    private DaoProdotto productDAO;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
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

        int productId = Integer.parseInt(request.getParameter("prodottoId"));

        if (servletPath.equals("/Remove")) {
            try {
                productDAO.deleteProdotto(productId);
                cartItemDAO.removeAllDeletedItems(productId);

            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "An error occurred while removing product.");
                return;
            }
        } else if (servletPath.equals("/Update")) {
            try {
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                BigDecimal price = new BigDecimal(request.getParameter("price"));
                double iva = Double.parseDouble(request.getParameter("iva"));
                String imagePath = request.getParameter("imagePath");
                Prodotto existingProdotto = productDAO.getProdottoById(productId);
                existingProdotto.setNomeProdotto(name);
                existingProdotto.setDescrizione(description);
                existingProdotto.setPrezzo(price);
                existingProdotto.setIva(iva);
                existingProdotto.setPath_immagine(imagePath);

                productDAO.update(existingProdotto);

            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "An error occurred while updating product." + e);
                return;
            }
        }
        response.sendRedirect("AdminCatalogPage");

    }

    @Override
    public void init() {
        productDAO = new DaoProdotto(DBConnection.getDataSource());
        cartItemDAO = new DaoComposizione(DBConnection.getDataSource());
    }
}

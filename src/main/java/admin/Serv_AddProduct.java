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
        String categoria = request.getParameter("categoria");
        String description = request.getParameter("descrizione");
        BigDecimal price = new BigDecimal(request.getParameter("prezzo"));
        String materiale = request.getParameter("materiale");
        String tipo = request.getParameter("tipo");

        String imagePath = request.getParameter("img");
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
        double d = 22;
        Prodotto prodotto = new Prodotto();
        prodotto.setNomeProdotto(name);
        prodotto.setCategoria(categoria);
        prodotto.setDescrizione(description);
        prodotto.setPrezzo(price);
        prodotto.setMateriale(materiale);
        prodotto.setTipo(tipo);
        prodotto.setDataInserimento(new java.util.Date());
        prodotto.setPath_immagine(imagePath);
        prodotto.setSconto(0);
        prodotto.setIva(d);



        try {
            prodottoDAO.createProdotto(prodotto);
            response.sendRedirect("ContextCheck?categoria=" + categoria);

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
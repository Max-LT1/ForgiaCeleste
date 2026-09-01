package Control;


import DAO.ClienteDAO;
import DAO.DBConnection;
import DAO.DaoComposizione;
import DAO.DaoProdotto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Client;
import model.Composizione;
import model.Prodotto;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet({"/HomePage", "/ContextCheck", "/AdmCat", "/SingleItem"})
public class Serv_product extends HttpServlet {
    private DataSource dataSource;
    private DaoProdotto daoProdotto;

    public void init() throws ServletException {
        dataSource = DBConnection.getDataSource();
        daoProdotto = new DaoProdotto(dataSource);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String servletPath = req.getServletPath();

        try {
            switch (servletPath) {
                case "/ContextCheck":
                    mostraCodex(req, resp);
                    break;
                case "/HomePage":
                    mostraHome(req, resp);
                    break;
                case "/AdmCat":
                    AdmCart(req, resp);
                    break;
                case "/SingleItem":
                    singleItem(req, resp);
                    break;
                default:
                    //TODO
            }
        } catch (SQLException e) {
            throw new ServletException("Errore durante il caricamento dei prodotti", e);
        }
    }

    private void mostraCodex(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        String categoria = req.getParameter("categoria");

        String search = req.getParameter("ricerca");

        List<Prodotto> prodotti = new ArrayList<>();
        if(search != null) {
            for (Prodotto prodotto : daoProdotto.ListaProdotti()) {
                if(prodotto.getDescrizione().contains(search) ||
                        prodotto.getNomeProdotto().contains(search) ||
                        prodotto.getMateriale().contains(search) ||
                        prodotto.getTipo().contains(search)) {
                    prodotti.add(prodotto);
                }
            }
            Set<String> categorie = new LinkedHashSet<>();
            Set<String> tipi = new LinkedHashSet<>();
            Set<String> materiali = new LinkedHashSet<>();
            for (Prodotto prodotto : prodotti) {
                if (prodotto.getCategoria() != null && !prodotto.getCategoria().isBlank()) {
                    categorie.add(prodotto.getCategoria());
                }
                if (prodotto.getTipo() != null && !prodotto.getTipo().isBlank()) {
                    tipi.add(prodotto.getTipo());
                }
                if (prodotto.getMateriale() != null && !prodotto.getMateriale().isBlank()) {
                    materiali.add(prodotto.getMateriale());
                }
            }

            req.setAttribute("Ricerca", search);
            req.setAttribute("Categoria", null);
            req.setAttribute("Categorie", categorie);
            req.setAttribute("Lista", prodotti);
            req.setAttribute("Tipi", tipi);
            req.setAttribute("Materiali", materiali);
            req.getRequestDispatcher("/codex.jsp").forward(req, resp);
        }else{
            prodotti = daoProdotto.prodottiPerCategoria(categoria);
            if (prodotti == null) {
                prodotti = new ArrayList<>();
            }
            Set<String> tipi = new LinkedHashSet<>();
            Set<String> materiali = new LinkedHashSet<>();
            for (Prodotto prodotto : prodotti) {
                if (prodotto.getTipo() != null && !prodotto.getTipo().isBlank()) {
                    tipi.add(prodotto.getTipo());
                }
                if (prodotto.getMateriale() != null && !prodotto.getMateriale().isBlank()) {
                    materiali.add(prodotto.getMateriale());
                }
            }
            req.setAttribute("Ricerca", null);
            req.setAttribute("Categoria", categoria);
            req.setAttribute("Categorie", new LinkedHashSet<>());
            req.setAttribute("Lista", prodotti);
            req.setAttribute("Tipi", tipi);
            req.setAttribute("Materiali", materiali);
            req.getRequestDispatcher("/codex.jsp").forward(req, resp);
        }
    }

    private void mostraHome(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        req.setAttribute("ListaSconti", daoProdotto.getAllSconto());
        req.setAttribute("NuoviProdotti", daoProdotto.getLatestadds());
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    private void singleItem(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Prodotto p = daoProdotto.getProdottoById(id);
        req.setAttribute("prodotto", p);
        req.getRequestDispatcher("/product.jsp").forward(req, resp);
    }

    private void AdmCart(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        req.setAttribute("ListaProdotti", daoProdotto.listaProdottiAdmn());
        req.getRequestDispatcher("/Admin/AdmCatalogo.jsp").forward(req, resp);
    }
}

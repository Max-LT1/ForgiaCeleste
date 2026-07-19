package Control;


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

import java.io.IOException;
import java.util.List;

@WebServlet("/carrelloServ")
public class Serv_Carrello extends HttpServlet {
    private static final long serialVersionUID = 2L;
    private DaoProdotto daoProdotto;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<Composizione> composizione = null;
        if((Client) session.getAttribute("cliente") == null){
            composizione = (List<Composizione>) session.getAttribute("guesscarrello");
        }else{
            composizione = (List<Composizione>) session.getAttribute("carrello");
        }

    }

}

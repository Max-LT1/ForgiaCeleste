package Control;


import DAO.ClienteDAO;
import DAO.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;

@WebServlet("/Profilo")
public class Serv_profile extends HttpServlet {
    private static final long serialVersionUID = 5L;
    private ClienteDAO clienteDAO;
    private DataSource dataSource;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


    public void init() throws ServletException {
        dataSource = DBConnection.getDataSource();
        clienteDAO = new ClienteDAO(dataSource);
    }
}

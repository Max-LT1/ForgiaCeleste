package admin;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAO.ClienteDAO;
import DAO.DBConnection;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Client;

import javax.sql.DataSource;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        DataSource ds = DBConnection.getDataSource();
        try {

            Connection connection = ds.getConnection();
            String message = "Ayo il db è andato good godos.";
            out.println("<html><body>");
            out.println("<h1>" + message + "</h1>");
            out.println("</body></html>");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void destroy() {
    }
}
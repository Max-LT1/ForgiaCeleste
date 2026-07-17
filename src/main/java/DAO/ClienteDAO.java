package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import model.Client;

public class ClienteDAO {
    private DataSource dataSource;

    public ClienteDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addCliente(Client cliente) throws SQLException {
        String query = "INSERT INTO Cliente (username, pwd, email, ruolo_cliente) VALUES (?, ?, ?, ?)";
        String checkQuery = "SELECT COUNT(*) FROM Cliente WHERE username = ? OR email = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
             PreparedStatement statement = connection.prepareStatement(query)) {
            checkStatement.setString(1, cliente.getUsername());
            checkStatement.setString(2, cliente.getEmail());

            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    throw new SQLIntegrityConstraintViolationException("Username o email già esistenti.");
                }
            }

            statement.setString(1, cliente.getUsername());
            statement.setString(2, cliente.getPassword());
            statement.setString(3, cliente.getEmail());
            statement.setString(4, cliente.getRuolo_cliente());

            statement.executeUpdate();
        }
    }


    public void cancellaCliente(String username, String password) throws SQLException {
        String query = "DELETE FROM Cliente WHERE username = ? AND pwd = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
        }
    }

    private Client extractClienteFromResultSet(ResultSet resultSet) throws SQLException {
        Client cliente = new Client();
        cliente.setUsername(resultSet.getString("username"));
        cliente.setEmail(resultSet.getString("email"));
        cliente.setPassword(resultSet.getString("pwd"));
        cliente.setNome(resultSet.getString("nome"));
        cliente.setCognome(resultSet.getString("cognome"));
        cliente.setIndirizzo(resultSet.getString("indirizzo"));
        cliente.setCitta(resultSet.getString("citta"));
        cliente.setProvincia(resultSet.getString("provincia"));
        cliente.setCap(resultSet.getString("cap"));
        cliente.setRuolo_cliente(resultSet.getString("ruolo_cliente"));
        return cliente;
    }

    public List<Client> getAllUsers() throws SQLException {
        String query = "SELECT * FROM Cliente";
        List<Client> userList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Client cliente = new Client();
                cliente.setUsername(resultSet.getString("username"));
                cliente.setPassword(resultSet.getString("pwd"));
                cliente.setEmail(resultSet.getString("email"));
                cliente.setRuolo_cliente(resultSet.getString("ruolo_cliente"));

                userList.add(cliente);
            }
        }

        return userList;
    }

    public Client getClienteByEmail(String email) throws SQLException {
        String query = "SELECT * FROM Cliente WHERE email = ?";
        try (Connection conn = dataSource.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractClienteFromResultSet(resultSet);
                }
            }
        }
        return null;
    }

    public Client getClienteByUsername(String username) throws SQLException {
        String query = "SELECT * FROM Cliente WHERE username = ?";
        try (Connection conn = dataSource.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractClienteFromResultSet(resultSet);
                }
            }
        }
        return null;
    }

    public Client getClienteByUsernamePassword(String Username, String Password) throws SQLException {
        String query = "SELECT * FROM Cliente WHERE username = ? AND pwd = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, Username);
            statement.setString(2, Password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Client cliente = new Client();
                    cliente.setUsername(resultSet.getString("username"));
                    cliente.setPassword(resultSet.getString("pwd"));
                    cliente.setEmail(resultSet.getString("email"));
                    cliente.setCap(resultSet.getString("cap"));
                    cliente.setCitta(resultSet.getString("citta"));
                    cliente.setCognome(resultSet.getString("cognome"));
                    cliente.setNome(resultSet.getString("nome"));
                    cliente.setIndirizzo(resultSet.getString("indirizzo"));
                    cliente.setProvincia(resultSet.getString("provincia"));
                    cliente.setRuolo_cliente(resultSet.getString("ruolo_cliente"));

                    return cliente;
                }
            }
        }

        return null;
    }

    public void updateCliente(Client cliente) throws SQLException {
        String query = "UPDATE Cliente SET nome = ?, cognome = ?, citta = ?, provincia = ?, indirizzo = ?, cap = ? WHERE username = ? AND pwd = ?";
        try (Connection conn = dataSource.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getCognome());
            statement.setString(3, cliente.getCitta());
            statement.setString(4, cliente.getProvincia());
            statement.setString(5, cliente.getIndirizzo());
            statement.setString(6, cliente.getCap());
            statement.setString(7, cliente.getUsername());
            statement.setString(8, cliente.getPassword());

            statement.executeUpdate();
        }
    }

}
package DAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import model.Composizione;

import javax.sql.DataSource;

public class DaoComposizione {
    private DataSource dataSource;

    public DaoComposizione(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public List<Composizione> findOrderedItemsByOrderId(String username, String email, int orderId)
            throws SQLException {
        List<Composizione> composizioni = new ArrayList<>();
        String query = "SELECT * FROM composizione WHERE username_cli = ? AND email_cli = ?  AND id_ordine = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setInt(3, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Composizione composizione = new Composizione();
                    composizione.setIdComposizione(resultSet.getInt("composizione_id"));
                    composizione.setIdOrdine(resultSet.getInt("id_ordine"));
                    composizione.setIdProdotto(resultSet.getInt("id_prodotto"));
                    composizione.setUsername(resultSet.getString("username_cli"));
                    composizione.setEmail(resultSet.getString("email_cli"));
                    composizione.setQuantita_prodotto(resultSet.getInt("quantita"));
                    composizione.setPrezzo_vendita(resultSet.getBigDecimal("prezzo_prodotto"));
                    composizioni.add(composizione);
                }
            }
        }
        return composizioni;
    }

    public List<Composizione> getComposizioniByUsernameAndEmail(String username, String email) throws SQLException {
        List<Composizione> composizioni = new ArrayList<>();
        String query = "SELECT * FROM composizione WHERE username_cli = ? AND email_cli = ? AND id_ordine IS NULL";
        try (Connection conn = dataSource.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int quantita = resultSet.getInt("quantita");
                    int idProdotto = resultSet.getInt("ID_prodotto");
                    String username_cli = resultSet.getString("username_cli");
                    String email_cli = resultSet.getString("email_cli");
                    // Altri campi da recuperare se necessario

                    Composizione composizione = new Composizione();
                    composizione.setIdProdotto(idProdotto);
                    composizione.setQuantita_prodotto(quantita);
                    composizione.setUsername(username_cli);
                    composizione.setEmail(email_cli);
                    composizioni.add(composizione);
                }
            }
        }
        return composizioni;
    }

    public void insertComposizione(Composizione composizione) throws SQLException {
        String query = "INSERT INTO composizione (id_prodotto, id_ordine) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, composizione.getIdProdotto());
            statement.setInt(2, composizione.getIdOrdine());
            statement.executeUpdate();
        }
    }

    public void removeAllDeletedItems(int productId) throws SQLException {
        String query = "DELETE FROM composizione WHERE id_ordine IS NULL AND id_prodotto = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            statement.executeUpdate();

        }
    }

    public void removeComposizione(String username, String email, int idProdotto) throws SQLException {
        String query = "DELETE FROM composizione WHERE username_cli = ? AND email_cli = ? AND id_prodotto = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setInt(3, idProdotto);
            statement.executeUpdate();
        }
    }
    // Altre operazioni CRUD per Composizione

    public void saveAllComposizioni(List<Composizione> composizioni) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            for (Composizione composizione : composizioni) {
                String user_email = composizione.getEmail();
                String user_username = composizione.getUsername();

                int productId = composizione.getIdProdotto();
                int quantity = composizione.getQuantita_prodotto();
                // Check if the productId and user_id exist in the table.
                String sql = "SELECT * FROM composizione WHERE username_cli = ? AND email_cli = ? AND id_ordine IS NULL";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, user_username);
                    statement.setString(2, user_email);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        boolean productIdExists = false;

                        // Iterate over the result set
                        while (resultSet.next()) {
                            int fetchedProductId = resultSet.getInt("id_prodotto");
                            int fetchedOrderId = resultSet.getInt("id_ordine");

                            if (fetchedOrderId == 0 && fetchedProductId == productId) {

                                // Update quantity
                                productIdExists = true;
                                sql = "UPDATE Composizione SET quantita = ? WHERE id_prodotto = ? AND username_cli = ? AND email_cli = ? AND id_ordine IS NULL";
                                try (PreparedStatement updateStatement = connection.prepareStatement(sql)) {
                                    updateStatement.setInt(1, quantity);
                                    updateStatement.setInt(2, productId);
                                    updateStatement.setString(3, user_username);
                                    updateStatement.setString(4, user_email);
                                    updateStatement.executeUpdate();
                                }
                            }
                        }

                        if (!productIdExists) {
                            // Insert a new row since productId is different from every row
                            // Perform the insert operation here
                            sql = "INSERT INTO Composizione (id_prodotto, quantita, id_ordine, username_cli, email_cli) VALUES (?, ?, NULL, ?, ?)";
                            try (PreparedStatement insertStatement = connection.prepareStatement(sql)) {
                                insertStatement.setInt(1, productId);
                                insertStatement.setInt(2, quantity);
                                insertStatement.setString(3, user_username);
                                insertStatement.setString(4, user_email);
                                insertStatement.executeUpdate();
                            }
                        }
                    }
                }
            }
        }
    }

    public void updateComposizione(int orderId, String username, String email, BigDecimal price, int productId)
            throws SQLException {
        String query = "UPDATE Composizione SET id_ordine = ?, prezzo_prodotto = ? WHERE username_cli = ? AND email_cli = ? AND id_ordine IS NULL AND id_prodotto = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            statement.setBigDecimal(2, price);
            statement.setString(3, username);
            statement.setString(4, email);
            statement.setInt(5, productId);
            statement.executeUpdate();
        }
    }
}

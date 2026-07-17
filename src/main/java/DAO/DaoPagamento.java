package DAO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import model.Pagamento;

public class DaoPagamento {
    private DataSource dataSource;

    public DaoPagamento(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertPagamento(Pagamento pagamento) throws SQLException {
        String query = "INSERT INTO Pagamento (ID_pagamento, data_pag, importo_pag,num_carta, data_scadenza, "
                + "titolare_conto,id_ordine) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, pagamento.getIdPagamento());
            statement.setDate(2, (Date) pagamento.getDataPagamento());
            statement.setBigDecimal(3, pagamento.getImportoPagamento());
            statement.setString(4, pagamento.getNumeroCarta());
            statement.setDate(5, (Date) pagamento.getDataScadenza());
            statement.setString(6, pagamento.getTitolareConto());
            statement.setInt(7, pagamento.getIdOrdine());
            statement.executeUpdate();
        }
    }

}

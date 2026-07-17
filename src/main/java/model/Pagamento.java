package model;

import java.math.BigDecimal;
import java.util.Date;

public class Pagamento {
    private Date dataPagamento;
    private Date dataScadenza;
    private int idOrdine;
    private int idPagamento;
    private BigDecimal importoPagamento;
    private String numeroCarta;
    private String titolareConto;

    public Pagamento() {}

    public Pagamento(Date dataPagamento, Date dataScadenza, int idOrdine, int idPagamento, BigDecimal importoPagamento, String numeroCarta, String titolareConto) {
        this.dataPagamento = dataPagamento;
        this.dataScadenza = dataScadenza;
        this.idOrdine = idOrdine;
        this.idPagamento = idPagamento;
        this.importoPagamento = importoPagamento;
        this.numeroCarta = numeroCarta;
        this.titolareConto = titolareConto;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Date getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public int getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(int idPagamento) {
        this.idPagamento = idPagamento;
    }

    public BigDecimal getImportoPagamento() {
        return importoPagamento;
    }

    public void setImportoPagamento(BigDecimal importoPagamento) {
        this.importoPagamento = importoPagamento;
    }

    public String getNumeroCarta() {
        return numeroCarta;
    }

    public void setNumeroCarta(String numeroCarta) {
        this.numeroCarta = numeroCarta;
    }

    public String getTitolareConto() {
        return titolareConto;
    }

    public void setTitolareConto(String titolareConto) {
        this.titolareConto = titolareConto;
    }
}

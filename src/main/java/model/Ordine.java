package model;

import java.math.BigDecimal;
import java.util.Date;

public class Ordine {
    private Date dataInserimento;
    private String email_cli;
    private int idOrdine;
    private double ivaCout;
    private BigDecimal prezzoVendita;
    private String statoOrdine;
    private String username_cli;

    public Ordine() {}

    public Ordine(Date dataInserimento, String email_cli, int idOrdine, double ivaCout, BigDecimal prezzoVendita, String statoOrdine, String username_cli) {
        this.dataInserimento = dataInserimento;
        this.email_cli = email_cli;
        this.idOrdine = idOrdine;
        this.ivaCout = ivaCout;
        this.prezzoVendita = prezzoVendita;
        this.statoOrdine = statoOrdine;
        this.username_cli = username_cli;
    }

    public Date getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Date dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    public String getEmailCliente() {
        return email_cli;
    }

    public void setEmailCliente(String email_cli) {
        this.email_cli = email_cli;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public double getIvaCout() {
        return ivaCout;
    }

    public void setIvaCout(double ivaCout) {
        this.ivaCout = ivaCout;
    }

    public BigDecimal getPrezzoVendita() {
        return prezzoVendita;
    }

    public void setPrezzoVendita(BigDecimal prezzoVendita) {
        this.prezzoVendita = prezzoVendita;
    }

    public String getStatoOrdine() {
        return statoOrdine;
    }

    public void setStatoOrdine(String statoOrdine) {
        this.statoOrdine = statoOrdine;
    }

    public String getUsernameCliente() {
        return username_cli;
    }

    public void setUsernameCliente(String username_cli) {
        this.username_cli = username_cli;
    }
}

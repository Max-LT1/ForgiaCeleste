package model;

public class Client {
    private String cap;
    private String citta;
    private String cognome;
    private String email;
    private String indirizzo;
    private String nome;
    private String password;
    private String provincia;
    private String ruolo_cliente;
    private String username;

    public Client(String cap, String citta, String cognome, String email, String indirizzo, String nome, String password, String provincia, String ruolo_cliente, String username) {
        this.cap = cap;
        this.citta = citta;
        this.cognome = cognome;
        this.email = email;
        this.indirizzo = indirizzo;
        this.nome = nome;
        this.password = password;
        this.provincia = provincia;
        this.ruolo_cliente = ruolo_cliente;
        this.username = username;
    }

    public Client() {}
    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getRuolo_cliente() {
        return ruolo_cliente;
    }

    public void setRuolo_cliente(String ruolo_cliente) {
        this.ruolo_cliente = ruolo_cliente;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

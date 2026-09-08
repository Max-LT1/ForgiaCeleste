```jsp
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="model.Ordine" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="model.Prodotto" %>
<%@ page import="model.Composizione" %>

<%
    String ctx = request.getContextPath();

    @SuppressWarnings("unchecked")
    List<Ordine> ordini = (List<Ordine>) request.getAttribute("ordineList");

    @SuppressWarnings("unchecked")
    List<Prodotto> prodotti = (List<Prodotto>) request.getAttribute("productList");

    @SuppressWarnings("unchecked")
    List<Composizione> composizioni = (List<Composizione>) request.getAttribute("compositionList");

    NumberFormat euroFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);

    int numeroOrdini = (ordini != null) ? ordini.size() : 0;
%>

<!DOCTYPE html>
<html lang="it">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Elenco Ordini</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cinzel:wght@400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= ctx %>/styles/style.css">
    <link rel="stylesheet" href="<%= ctx %>/styles/search-bar.css">
    <link rel="stylesheet" href="<%= ctx %>/styles/ordini.css">

</head>

<body>

<jsp:include page="../nav-bar.jsp" />


<main class="orders-page">
    <section class="orders-card">
        <div class="orders-header">
            <div>
                <p class="section-label">
                    Archivio della forgia
                </p>
                <h1>
                    Elenco Ordini
                </h1>
                <p class="orders-description">
                    Visualizza gli ordini effettuati e i relativi prodotti.
                </p>
            </div>
            <div class="orders-counter">
                <span>
                    <%= numeroOrdini %>
                </span>
                ordini
            </div>
        </div>
        <%
            if (ordini == null || ordini.isEmpty()) {
        %>
        <div class="empty-orders">
            <div class="empty-orders-icon">
                ◇
            </div>
            <h2>
                Nessun ordine presente
            </h2>
            <p>
                Non sono ancora stati effettuati ordini.
            </p>
        </div>
        <%
        } else {
        %>
        <div class="orders-list">
            <%
                for (Ordine ordine : ordini) {
                    int idOrdine = ordine.getIdOrdine();
                    String totaleFormattato = euroFormat.format(ordine.getPrezzoVendita());
            %>
            <article class="order-item">
                <button
                        type="button"
                        class="order-header"
                        aria-expanded="false"
                        aria-controls="order-details-<%= idOrdine %>">
                    <div class="order-main-info">
                        <div class="order-number">
                            <span class="order-label">
                                Ordine
                            </span>
                            <strong>
                                #<%= idOrdine %>
                            </strong>
                        </div>
                        <div class="order-customer">
                            <span class="order-label">
                                Cliente
                            </span>
                            <strong>
                                <%= ordine.getUsernameCliente() %>
                            </strong>
                        </div>
                    </div>
                    <div class="order-summary">
                        <div class="order-total">
                            <span class="order-label">
                                Totale pagato
                            </span>
                            <strong>
                                <%= totaleFormattato %>
                            </strong>
                        </div>
                        <span
                            class="order-chevron"
                            aria-hidden="true">
                                ⌄
                            </span>
                    </div>
                </button>
                <div
                        id="order-details-<%= idOrdine %>"
                        class="order-details"
                        hidden>
                    <div class="order-details-inner">
                        <div class="order-products-header">
                            <span>
                                Prodotto
                            </span>
                            <span>
                                Quantità
                            </span>
                            <span>
                                Prezzo unitario
                            </span>
                            <span>
                                Totale
                            </span>
                        </div>
                        <div class="order-products">
                            <%
                                boolean flag = true;
                                for(Composizione composizione: composizioni){
                                    for(Prodotto prodotto : prodotti){
                                        if(composizione.getIdOrdine() == ordine.getIdOrdine() && prodotto.getIdProdotto() == composizione.getIdProdotto()){
                                            String nome = prodotto.getNomeProdotto();
                                            int quantita = composizione.getQuantita_prodotto();
                                            BigDecimal prezzoUnitario = prodotto.getPrezzo();
                                            BigDecimal subtotale = prodotto.getPrezzo();
                                            flag = false;
                            %>
                            <div class="order-product">
                                <div class="product-info">
                                    <strong>
                                        <%= nome %>
                                    </strong>
                                </div>
                                <div
                                    class="product-quantity"
                                    data-label="Quantità">
                                        <span>
                                            <%= quantita %>
                                        </span>
                                </div>
                                <div
                                    class="product-unit-price"
                                    data-label="Prezzo unitario">
                                    <span>
                                        <%= euroFormat.format(prezzoUnitario) %>
                                    </span>
                                </div>
                                <div
                                    class="product-subtotal"
                                    data-label="Totale">
                                    <strong>
                                        <%= euroFormat.format(subtotale) %>
                                    </strong>
                                </div>
                            </div>
                            <%
                                        }
                                    }
                                }
                                if(flag) {
                            %>
                            <div class="empty-order-products">
                                Nessun prodotto associato a questo ordine.
                            </div>
                            <%
                                }
                            %>
                        </div>
                        <div class="order-details-footer">
                            <span>
                                Totale ordine
                            </span>
                            <strong>
                                <%= totaleFormattato %>
                            </strong>
                        </div>
                    </div>
                </div>
            </article>
            <%
                }
            %>
        </div>
        <%
            }
        %>
    </section>
</main>

<script src="../scripts/admOrdini.js"></script>
<script src="../scripts/user-overlay.js"></script>

</body>

</html>

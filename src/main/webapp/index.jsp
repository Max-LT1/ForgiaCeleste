<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List"%>
<%@ page import="model.Prodotto"%>
<%@ page import="java.util.Locale" %>
<%@ page import="java.text.NumberFormat" %>

<%!
    private String escapeHtml(Object value) {
        if (value == null) {
            return "";
        }

        return String.valueOf(value)
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
    private String encodeUrl(String value) {
        if (value == null) {
            return "";
        }

        try {
            return java.net.URLEncoder.encode(
                    value,
                    java.nio.charset.StandardCharsets.UTF_8
            );
        } catch (Exception exception) {
            return "";
        }
    }
%>


<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Forgia Celeste</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cinzel:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="./styles/style.css">
    <link rel="stylesheet" href="./styles/style-home.css">
    <link rel="stylesheet" href="./styles/search-bar.css">
</head>
<body>

<jsp:include page="nav-bar.jsp" />

<main class="content">
    <section class="group">
        <h2>Novità</h2>

        <div class="carousel-container">
            <button class="arrow left">&#9664;</button>
            <div class="carousel" id="carousel1">
                <%
                    String contextPath = request.getContextPath();
                    NumberFormat formatoPrezzo =
                            NumberFormat.getCurrencyInstance(Locale.ITALY);
                    List<Prodotto> prodottiNuovi = (List<Prodotto>) request.getAttribute("NuoviProdotti");
                %>
                <%
                    if (prodottiNuovi == null || prodottiNuovi.isEmpty()) {
                %>
                <p>Nessun prodotto disponibile</p>
                <%
                } else {
                %>
                <%
                    for (Prodotto prodotto : prodottiNuovi) {
                        if (prodotto == null) {
                            continue;
                        }

                        String id =
                                String.valueOf(prodotto.getIdProdotto());

                        String nome =
                                prodotto.getNomeProdotto() != null
                                        ? prodotto.getNomeProdotto()
                                        : "Prodotto senza nome";

                        String categoria =
                                prodotto.getCategoria() != null
                                        ? prodotto.getCategoria()
                                        : "Non specificato";

                        String tipo =
                                prodotto.getTipo() != null
                                        ? prodotto.getTipo()
                                        : "Non specificato";

                        String materiale =
                                prodotto.getMateriale() != null
                                        ? prodotto.getMateriale()
                                        : "Non specificato";

                        String immagine =
                                prodotto.getPath_immagine() != null ? prodotto.getPath_immagine() : "images/placeholder-product.png";

                        String prezzoFormattato = formatoPrezzo.format(prodotto.getPrezzo());
                %>
                <article class="product" data-id="<%= escapeHtml(id) %>" data-categoria="<%= escapeHtml(categoria) %>" data-tipo="<%= escapeHtml(tipo) %>" data-materiale="<%= escapeHtml(materiale) %>">
                    <a class="product-link" href="<%= contextPath %>/SingleItem?id=<%= id %>" aria-label="Apri <%= escapeHtml(nome) %>">
                        <img class="product-image" src="<%= escapeHtml(immagine) %>" alt="<%= escapeHtml(nome) %>" loading="lazy" onerror="this.onerror=null; this.src='<%= contextPath %>/images/placeholder-product.png';">
                        <div class="product-info">
                            <%= escapeHtml(nome) %><br>
                            <%= escapeHtml(prezzoFormattato) %>
                        </div>
                    </a>
                </article>
                <%      }
                    }%>
            </div>

            <button class="arrow right">&#9654;</button>
        </div>
    </section>

    <section class="group">
        <h2>Sconti</h2>
        <div class="carousel-container">
            <button class="arrow left">&#9664;</button>
            <div class="carousel" id="carousel2">
                <%
                    List<Prodotto> prodottiScontati = (List<Prodotto>) request.getAttribute("ListaSconti");
                %>
                <%
                    if (prodottiScontati == null || prodottiScontati.isEmpty()) {
                %>
                <p>Nessun prodotto disponibile</p>
                <%
                } else {
                    for (Prodotto prodotto : prodottiScontati) {
                        if (prodotto == null) {
                            continue;
                        }

                        String id =
                                String.valueOf(prodotto.getIdProdotto());

                        String nome =
                                prodotto.getNomeProdotto() != null
                                        ? prodotto.getNomeProdotto()
                                        : "Prodotto senza nome";

                        String categoria =
                                prodotto.getCategoria() != null
                                        ? prodotto.getCategoria()
                                        : "Non specificato";

                        String tipo =
                                prodotto.getTipo() != null
                                        ? prodotto.getTipo()
                                        : "Non specificato";

                        String materiale =
                                prodotto.getMateriale() != null
                                        ? prodotto.getMateriale()
                                        : "Non specificato";

                        String immagine =
                                prodotto.getPath_immagine() != null ? prodotto.getPath_immagine() : "images/placeholder-product.png";

                        String prezzoFormattato = formatoPrezzo.format(prodotto.getPrezzo());
                %>
                <article class="product" data-id="<%= escapeHtml(id) %>" data-categoria="<%= escapeHtml(categoria) %>" data-tipo="<%= escapeHtml(tipo) %>" data-materiale="<%= escapeHtml(materiale) %>">
                    <a class="product-link" href="<%= contextPath %>/SingleItem?id=<%= id %>" aria-label="Apri <%= escapeHtml(nome) %>">
                        <img class="product-image" src="<%= escapeHtml(immagine) %>" alt="<%= escapeHtml(nome) %>" loading="lazy" onerror="this.onerror=null; this.src='<%= contextPath %>/images/placeholder-product.png';">
                        <div class="product-info">
                            <%= escapeHtml(nome) %><br>
                            <%= escapeHtml(prezzoFormattato) %>
                        </div>
                    </a>
                </article>
                <% }
                    }%>
            </div>
            <button class="arrow right">&#9654;</button>
        </div>
    </section>

</main>
<footer class="footer">
    <p>© Forgia Medievale</p>
</footer>

<!-- CAROSELLO -->
<script src="scripts/carosello.js"></script>

</body>
</html>

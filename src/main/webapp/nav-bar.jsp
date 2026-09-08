<%@ page import="model.Client" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Client cliente = (Client) session.getAttribute("cliente");
    boolean utenteLoggato = cliente != null;
    String ctx = request.getContextPath();
%>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<header class="navbar">
    <form class="navbar-search" action="<%= ctx %>/ContextCheck?" method="get">
        <input class="searchbar" type="search" name="ricerca" placeholder="Cerca nel Codex..." aria-label="Cerca prodotti" required>
        <button class="search-button" type="submit" aria-label="Avvia ricerca">Cerca</button>
    </form>
    <nav class="nav-links">
        <a href="<%= ctx %>/HomePage" class="navbar-logo">Home</a>
        <a href="<%= ctx %>/ContextCheck?categoria=armi">Armi</a>
        <a href="<%= ctx %>/ContextCheck?categoria=armature">Armature</a>
        <a href="<%= ctx %>/ContextCheck?categoria=scudi">Scudi</a>
        <a href="<%= ctx %>/ContextCheck?categoria=accessori">Accessori</a>

        <%
            if(utenteLoggato){
                if(cliente.getRuolo_cliente().equals("admin")){
                    %>
        <a href="<%=ctx%>/admin/AdmOrdini.jsp">Ordini</a>
        <a href="<%=ctx%>/admin/AdmAdd.jsp">Aggiungi Prodotto</a>

        <%
                }
            }
        %>



        <a href="<%= ctx %>/contacts.jsp">Contatti</a>



    </nav>
    <div class="user-area">
        <span class="icon-user" id="user-icon" role="button" tabindex="0" aria-label="Apri area utente">👤</span>
        <a href="<%= ctx %>/carrelloServ" class="navbar-icon cart-link" aria-label="Apri il carrello">
            <span class="icon-cart" id="cart-icon">🛒</span>
        </a>
    </div>
</header>
<!-- OVERLAY UTENTE -->
<div id="user-overlay" class="overlay hidden" role="dialog" aria-label="Area utente">
    <div class="overlay-content">
        <h3>Profilo</h3>
        <button type="button" class="overlay-close" data-close-overlay aria-label="Chiudi">&times;</button>
    </div>
    <% if (utenteLoggato) { %>
    <div class="user-summary">
        <div class="user-avatar" aria-hidden="true">👤</div>
        <div>
            <span class="overlay-label">Bentornato</span>
            <strong class="user-name"><%= cliente.getUsername() %></strong>
        </div>
    </div>
    <div class="overlay-actions">
        <a class="overlay-button primary" href="<%= ctx %>/user-area.jsp">Visualizza profilo</a>
        <form action="<%= ctx %>/logoutServ" method="post">
            <button type="submit" class="overlay-button danger">Logout</button>
        </form>
    </div>
    <% } else { %>
    <p class="overlay-message">Accedi al tuo account oppure crea un nuovo profilo.</p>
    <a class="overlay-button primary full-width" href="<%= ctx %>/log-sign.jsp">Accedi / Registrati</a>
    <% } %>
</div>

<script src="scripts/user-overlay.js"></script>
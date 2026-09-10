<%@ page import="model.Client" %>
<%--
  Created by IntelliJ IDEA.
  User: Luca Giammattei
  Date: 18/07/2026
  Time: 13:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
    private String escapeHtml(String valore) {
        if (valore == null) {
            return "";
        }

        return valore
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
    private String normalizzaPercorso(String percorso) {
        if (percorso == null) {
            return "";
        }

        percorso = percorso.trim();

        while (percorso.startsWith("/")) {
            percorso = percorso.substring(1);
        }

        return percorso;
    }
%>

<%
    Client cliente = (Client)session.getAttribute("cliente");
    String name = escapeHtml(cliente.getNome());
    String cognome = escapeHtml(cliente.getCognome());
    String provincia = escapeHtml(cliente.getProvincia());
    String username = escapeHtml(cliente.getUsername());
    String citta = escapeHtml(cliente.getCitta());
    String email = escapeHtml(cliente.getEmail());
    String indirizzo = escapeHtml(cliente.getIndirizzo());
    String cap = escapeHtml(cliente.getCap());

%>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Area Utente</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cinzel:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="./styles/style.css">
    <link rel="stylesheet" href="./styles/user-area.css">
    <link rel="stylesheet" href="./styles/style-home.css">
    <link rel="stylesheet" href="styles/search-bar.css">
</head>
<body>
<jsp:include page="nav-bar.jsp" />
<main>
    <div class="container">
        <section id="UserDataCard" class="card auth-card active-card">
            <h2 id="utente-Title">AREA UTENTE</h2>
            <form action="${pageContext.request.contextPath}/modificaUtente" method="post" id="modifyform">
                <input type="hidden" id="Ogusername" name="Ogusername" value="<%= username%>">
                <div class="anagrafica">

                    <label for="username">
                        Username
                    </label>
                    <input id="username" type="text" name="username" value="<%= username%>"><br>
                    <label for="name">
                        Nome
                    </label>
                    <input id="name" type="text" name="nome" value="<%= name%>"><br>
                    <label for="cognome">
                        cognome
                    </label>
                    <input id="cognome" type="text" name="cognome" value="<%= cognome%>"><br>
                    <label for="email">
                        email
                    </label>
                    <input id="email" type="email" name="email" value="<%= email%>"><br>

                    <div class="residenza">

                        <label for="citta">
                            città
                        </label>
                        <input id="citta" type="text" name="citta" value="<%= citta%>"><br>
                        <label for="provincia">
                            provincia
                        </label>
                        <input id="provincia" type="text" name="provincia" value="<%=provincia%>"><br>
                        <label for="cap">
                            Cap
                        </label>
                        <input id="cap" type="text" name="cap" value="<%= cap%>"><br>
                        <label for="indirizzo">
                            indirizzo
                        </label>
                        <input id="indirizzo" type="text" name="indirizzo" value="<%=indirizzo%>"><br>
                    </div>
                    <div class="password">
                        <label for="passwordAttuale">
                            Password
                        </label>
                        <input id="passwordAttuale" type="text" name="pswAttuale" required><br>
                        <label for="NuovaPassword">
                            NuovaPassword
                        </label>
                        <input id="NuovaPassword" type="text" name="NuovaPassword"><br>
                    </div>
                    <button type="submit" class="btn" formaction="modificaUtente">
                        Modifica
                    </button>
                </div>
            </form>
        </section>
    </div>
</main>


<script src="scripts/user-area.js"></script>
</body>
</html>

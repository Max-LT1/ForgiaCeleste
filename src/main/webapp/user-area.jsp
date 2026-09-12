<%@ page import="model.Client" %>
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
    String surname = escapeHtml(cliente.getCognome());
    String province = escapeHtml(cliente.getProvincia());
    String username = escapeHtml(cliente.getUsername());
    String city = escapeHtml(cliente.getCitta());
    String email = escapeHtml(cliente.getEmail());
    String address = escapeHtml(cliente.getIndirizzo());
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
    <link rel="stylesheet" href="styles/search-bar.css">
</head>
<body>
<jsp:include page="nav-bar.jsp" />
<main class="user-page">
    <div class="container">
        <section id="UserDataCard" class="card user-card">
            <h2 id="utente-Title">AREA UTENTE</h2>

            <form class="input-form" id="modifyForm" action="${pageContext.request.contextPath}/modificaUtente" method="post" >
                <input type="hidden" value="<%=username%>" id="OriginalUsername" name="OriginalUsername">
                <div class="input-group">
                    <label for="currentUsername">
                        Username
                    </label>
                    <input
                            id="currentUsername"
                            type="text"
                            name="username"
                            autocomplete="username"
                            placeholder="Inserisci il tuo username"
                            value="<%=username%>"
                            required>
                </div>
                <div class="row">
                    <div class="input-group">
                        <label for="currentName">
                            Nome
                        </label>
                        <input
                                id="currentName"
                                type="text"
                                name="nome"
                                autocomplete="given-name"
                                value="<%=name%>"
                                required>

                        <label for="currentSurname">
                            Cognome
                        </label>
                        <input
                                id="currentSurname"
                                type="text"
                                name="cognome"
                                value="<%=surname%>"
                                autocomplete="family-name"
                                required>
                    </div>
                </div>
                <div class="input-group">
                    <label for="currentEmail">
                        Email
                    </label>
                    <input
                            id="currentEmail"
                            type="email"
                            name="email"
                            autocomplete="email"
                            value="<%=email%>"
                            required>
                </div>
                <div class="input-group">
                    <label for="currentAddress">
                        Indirizzo
                    </label>
                    <input
                            id="currentAddress"
                            type="text"
                            name="indirizzo"
                            autocomplete="street-address"
                            value="<%=address%>"
                            required>
                </div>
                <div class="row">
                    <div class="input-group">
                        <label for="currentCity">
                            Città
                        </label>
                        <input
                                id="currentCity"
                                type="text"
                                name="citta"
                                autocomplete="address-level2"
                                value="<%=city%>"
                                required>
                    </div>
                    <div class="input-group">
                        <label for="currentProvince">
                            Provincia
                        </label>
                        <input
                                id="currentProvince"
                                type="text"
                                name="provincia"
                                maxlength="2"
                                placeholder="RM"
                                autocomplete="address-level1"
                                value="<%=province%>"
                                required>
                    </div>
                </div>
                <div class="input-group">
                    <label for="currentCap">
                        CAP
                    </label>
                    <input
                            id="currentCap"
                            type="text"
                            name="cap"
                            maxlength="5"
                            pattern="[0-9]{5}"
                            inputmode="numeric"
                            autocomplete="postal-code"
                            value="<%=cap%>"
                            required>
                </div>
                <div class="row">
                    <div class="input-group">
                        <label for="currentPassword">
                            Password
                        </label>
                        <input
                                id="currentPassword"
                                type="password"
                                name="password"
                                autocomplete="new-password"
                                required>
                    </div>
                    <div class="input-group">
                        <label for="NewPassword">
                            Nuova Password
                        </label>
                        <input
                                id="NewPassword"
                                type="password"
                                name="NewPassword"
                                autocomplete="new-password">
                    </div>

                    <div class="input-group">
                        <label for="confermaNewpsw">
                            Conferma Password
                        </label>
                        <input
                            id="confermanewPsw"
                            type="password"
                            name="confermanewPsw"
                            autocomplete="new-password">
                    </div>
                </div>
                <button type="submit" class="btn" formaction="${pageContext.request.contextPath}/modificaUtente">
                    Modifica
                </button>
            </form>
        </section>
    </div>
</main>


<script src="scripts/user-area.js"></script>

</body>
</html>

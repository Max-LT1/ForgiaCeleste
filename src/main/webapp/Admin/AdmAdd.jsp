<%--
  Created by IntelliJ IDEA.
  User: leona
  Date: 15/07/2026
  Time: 16:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Client"%>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CatalogoAdmin</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cinzel:wght@400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="../styles/style.css">
    <link rel="stylesheet" type="text/css" href="../styles/AddProdoct.css">
    <link rel="stylesheet" type="text/css" href="../styles/search-bar.css">
    <title>Title</title>
</head>
<body>
<%
    String sessionToken = (String) session.getAttribute("sessionToken");

    String clienterole = ((Client) session.getAttribute("cliente")).getRuolo_cliente();
    if (!(clienterole.equals("admin"))) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not an admin.");
        return;
    }
%>
<jsp:include page="../nav-bar.jsp"/>
<main class="auth-page">
    <div class="container">
        <section id="AddCard" class="card auth-card active-card" aria-labelledby="loginTitle">
            <h2 id="loginTitle">Aggiungi</h2>
            <!-- LoginServlet-->
            <form class="input-form" id="AddForm" action="<%=request.getContextPath()%>/addProdotto" method="post">
                <input type="hidden" name="clientToken" value="<%= session.getAttribute("sessionToken") %>">
                <div class="input-group">
                    <label for="nomeProdotto">
                        NomeProdotto
                    </label>
                    <input
                            id="nomeProdotto"
                            type="text"
                            name="name"
                            placeholder=""
                            required>
                </div>

                <div class="input-group">
                    <label for="categoria">
                        Categoria
                    </label>
                    <input
                            id="categoria"
                            type="text"
                            name="categoria"
                            placeholder=""
                            autocomplete="categoria"
                            required>
                </div>
                <div class="input-group">
                    <label for="prezzo">
                        prezzo
                    </label>
                    <input
                            id="prezzo"
                            type="number"
                            name="prezzo"
                            placeholder=""
                            required>
                </div>
                <div class="input-group">
                    <label for="descrizione">
                        descrizione
                    </label>
                    <textarea
                        id="descrizione"
                        name="descrizione"
                        placeholder=""
                        required>
                    </textarea>
                </div>
                <div class ="input-group">
                    <label for="materiale">
                        materiale
                    </label>
                    <input
                        id="materiale"
                        name="materiale"
                        type="text"
                        placeholder=""
                        required>
                </div>
                <div class="input-group">
                    <label for="tipo">
                        tipo
                    </label>
                    <input
                        id="tipo"
                        name="tipo"
                        type="text"
                        placeholder=""
                        required>
                </div>
                <div class="input-group">
                    <label for="img">
                        immagine (inserisci link)
                    </label>
                    <input
                        type="url"
                        id="img"
                        name="img"
                        required>
                </div>
                <button type="submit" class="btn">
                    Aggiungi
                </button>
            </form>
</main>



</body>
</html>

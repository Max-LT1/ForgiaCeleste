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
    <link rel="stylesheet" type="text/css" href="styles/style.css">
    <link rel="stylesheet" type="text/css" href="styles/search-bar.css">
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
</body>
</html>

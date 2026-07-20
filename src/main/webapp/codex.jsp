<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Codex - La Forgia Celeste</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cinzel:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/search-bar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/codex.css">
</head>
<body>

<jsp:include page="nav-bar.jsp" />

<main class="codex-page">
    <header class="codex-heading">
        <div>
            <span class="codex-eyebrow">Archivio della Forgia</span>
            <h1><c:out value="${empty Categoria ? 'Tutti i prodotti' : Categoria}" /></h1>
        </div>
        <p id="result-count" class="result-count" aria-live="polite"></p>
    </header>

    <section class="filter-panel" aria-label="Filtra prodotti per tipo">
        <span class="filter-label">Filtra per tipo</span>
        <div id="type-filters" class="filter-buttons">
            <button type="button" class="filter-button active" data-filter="all">Tutti</button>
            <c:forEach items="${Tipi}" var="tipo">
                <button type="button" class="filter-button" data-filter="${tipo}">
                    <c:out value="${tipo}" />
                </button>
            </c:forEach>
        </div>
    </section>

    <c:choose>
        <c:when test="${empty Lista}">
            <section class="empty-codex">
                <span aria-hidden="true">⚒</span>
                <h2>Nessun prodotto trovato</h2>
                <p>Non risultano oggetti disponibili per questa categoria.</p>
            </section>
        </c:when>
        <c:otherwise>
            <section id="product-grid" class="product-grid" aria-label="Elenco prodotti">
                <c:forEach items="${Lista}" var="prodotto">
                    <article class="codex-product"
                             data-id="${prodotto.id}"
                             data-tipo="${prodotto.tipo}">
                        <a class="product-link"
                           href="${pageContext.request.contextPath}/prodotto?id=${prodotto.id}"
                           aria-label="Apri ${prodotto.nome}">
                            <div class="product-image-wrapper">
                                <img class="product-image"
                                     src="${pageContext.request.contextPath}/${prodotto.immagine}"
                                     alt="${prodotto.nome}"
                                     loading="lazy"
                                     onerror="this.src='${pageContext.request.contextPath}/images/placeholder-product.png'">
                                <span class="price-badge">
                                    <fmt:formatNumber value="${prodotto.prezzo}" type="currency" currencySymbol="€" />
                                </span>
                            </div>
                            <div class="product-info">
                                <h2 class="product-title"><c:out value="${prodotto.nome}" /></h2>
                                <span class="product-type"><c:out value="${prodotto.tipo}" /></span>
                            </div>
                        </a>
                    </article>
                </c:forEach>
            </section>

            <section id="no-filter-results" class="empty-codex hidden" aria-live="polite">
                <span aria-hidden="true">⌕</span>
                <h2>Nessun oggetto di questo tipo</h2>
                <p>Seleziona un altro filtro per visualizzare i prodotti disponibili.</p>
            </section>
        </c:otherwise>
    </c:choose>
</main>

<script src="${pageContext.request.contextPath}/scripts/codex.js"></script>
</body>
</html>
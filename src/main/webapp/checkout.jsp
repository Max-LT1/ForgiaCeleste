<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Registrazione</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cinzel:wght@400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="styles/style.css">
    <link rel="stylesheet" href="styles/search-bar.css">
    <link rel="stylesheet" href="styles/style-log-sign.css">
</head>
<body>

<jsp:include page="nav-bar.jsp" />

<main class="auth-page">
    <div class="container">
        <section id="registerCard" class="card auth-card active-card" aria-labelledby="registerTitle">
            <h2 id="registerTitle">CHECKOUT</h2>
            <p class="auth-description">
                Compila i campi per terminare il checkout.
            </p>
            <form class="input-form" id="checkoutForm" action="Checkout" method="post">
                <input type="hidden" name="clientToken" value="${sessionScope.sessionToken}">
                <div class="row">
                    <div class="input-group">
                        <label for="registerName">
                            Nome
                        </label>
                        <input
                                id="registerName"
                                type="text"
                                name="nome"
                                autocomplete="given-name"
                                required>
                    </div>
                    <div class="input-group">
                        <label for="registerSurname">
                            Cognome
                        </label>
                        <input
                                id="registerSurname"
                                type="text"
                                name="cognome"
                                autocomplete="family-name"
                                required>
                    </div>
                </div>
                <div class="input-group">
                    <label for="registerCardNumber">
                        Numero Carta
                    </label>
                    <input
                            id="registerCardNumber"
                            type="text"
                            name="numero"
                            minlength="16"
                            maxlength="16"
                            placeholder="1111 2222 3333 4444"
                            autocomplete="cc-number"
                            required>
                </div>
                <div class="row">
                    <div class="input-group">
                        <label for="registerDate">
                            Mese Scadenza
                        </label>
                        <input
                                id="registerDate"
                                type="text"
                                name="mese"
                                minlength="2"
                                maxlength="2"
                                placeholder="12"
                                autocomplete="cc-exp-month"
                                required>
                    </div>
                    <div class="input-group">
                        <label for="registerDate">
                            Anno Scadenza
                        </label>
                        <input
                                id="registerDate"
                                type="text"
                                name="anno"
                                minlength="2"
                                maxlength="2"
                                placeholder="27"
                                autocomplete="cc-exp-year"
                                required>
                    </div>
                    <div class="input-group">
                        <label for="registercsc">
                            CVV
                        </label>
                        <input
                                id="registercsc"
                                type="text"
                                name="cvv"
                                minlength="3"
                                maxlength="3"
                                placeholder="000"
                                autocomplete="cc-csc"
                                required>
                    </div>
                </div>
                <button type="submit" class="btn">
                    CONFERMA PAGAMENTO
                </button>
            </form>
        </section>
    </div>
</main>

<script src="scripts/checkout.js"></script>

</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Sign Up</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

    <!-- Font Cinzel -->
    <link href="https://fonts.googleapis.com/css2?family=Cinzel:wght@400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="styles/style-log-sign.css">
</head>

<body>

<div class="container">
    <!-- LOGIN -->
    <div class="card">
        <h2>LOGIN</h2>
        <form action="LoginServlet" method="post">
            <div class="input-group">
                <label>Email</label>
                <input
                        type="email"
                        name="email"
                        placeholder="Inserisci la tua email"
                        required>
            </div>
            <div class="input-group">
                <label>Password</label>
                <input
                        type="password"
                        name="password"
                        placeholder="Inserisci la password"
                        required>
            </div>
            <div class="remember">
                <input type="checkbox" id="remember">
                <label for="remember">Ricordami</label>
            </div>
            <button type="submit" class="btn">LOGIN</button>
        </form>
    </div>

    <!-- SIGN IN -->
    <div class="card">
        <h2>SIGN UP</h2>
        <form action="RegisterServlet" method="post">
            <div class="row">
                <div class="input-group">
                    <label>Nome</label>
                    <input
                            type="text"
                            name="nome"
                            required>
                </div>
                <div class="input-group">
                    <label>Cognome</label>
                    <input
                            type="text"
                            name="cognome"
                            required>
                </div>
            </div>
            <div class="input-group">
                <label>Username</label>
                <input
                        type="text"
                        name="username"
                        required>
            </div>
            <div class="input-group">
                <label>Email</label>
                <input
                        type="email"
                        name="email"
                        required>
            </div>
            <div class="row">
                <div class="input-group">
                    <label>Password</label>
                    <input
                            type="password"
                            name="password"
                            required>
                </div>
                <div class="input-group">
                    <label>Conferma Password</label>
                    <input
                            type="password"
                            name="confermaPassword"
                            required>
                </div>
            </div>
            <div class="input-group">
                <label>Indirizzo</label>
                <input
                        type="text"
                        name="indirizzo"
                        required>
            </div>
            <div class="row">
                <div class="input-group">
                    <label>Città</label>
                    <input
                            type="text"
                            name="citta"
                            required>
                </div>
                <div class="input-group">
                    <label>Provincia</label>
                    <input
                            type="text"
                            name="provincia"
                            maxlength="2"
                            required>
                </div>
            </div>
            <div class="input-group">
                <label>CAP</label>
                <input
                        type="text"
                        name="cap"
                        maxlength="5"
                        pattern="[0-9]{5}"
                        required>
            </div>
            <button type="submit" class="btn">SIGN UP</button>
        </form>
    </div>
</div>

<script src="js/log-sign.js"></script>

</body>
</html>
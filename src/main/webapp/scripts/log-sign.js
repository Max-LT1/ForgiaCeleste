"use strict";

document.addEventListener("DOMContentLoaded", () => {
    const forms = document.querySelectorAll("form");

    const loginCard = document.getElementById("loginCard");
    const registerCard = document.getElementById("registerCard");

    const showRegisterButton = document.getElementById(
        "showRegisterButton"
    );

    const showLoginButton = document.getElementById(
        "showLoginButton"
    );

    const loginForm = document.getElementById("loginForm");
    const registerForm = document.getElementById("registerForm");

    if (loginForm) {
        inizializzaLogin(loginForm);
    }

    if (registerForm) {
        inizializzaRegistrazione(registerForm);
    }

    if (
        !loginCard ||
        !registerCard ||
        !showRegisterButton ||
        !showLoginButton
    ) {
        return;
    }

    showRegisterButton.addEventListener("click", () => {
        mostraRegistrazione();
    });

    showLoginButton.addEventListener("click", () => {
        mostraLogin();
    });

    function mostraRegistrazione() {
        nascondiCard(loginCard);
        mostraCard(registerCard);

        window.setTimeout(() => {
            const primoCampo = registerCard.querySelector(
                "input:not([type='hidden'])"
            );

            primoCampo?.focus();
        }, 250);
    }

    function mostraLogin() {
        nascondiCard(registerCard);
        mostraCard(loginCard);

        window.setTimeout(() => {
            const primoCampo = loginCard.querySelector(
                "input:not([type='hidden'])"
            );

            primoCampo?.focus();
        }, 250);
    }

    function mostraCard(card) {
        card.classList.remove("hidden-card");

        card.setAttribute("aria-hidden", "false");

        window.requestAnimationFrame(() => {
            card.classList.add("active-card");
        });
    }

    function nascondiCard(card) {
        card.classList.remove("active-card");

        card.setAttribute("aria-hidden", "true");

        window.setTimeout(() => {
            card.classList.add("hidden-card");
        }, 250);
    }
});

function inizializzaLogin(form) {
    const emailInput = form.querySelector('input[name="email"]');
    const passwordInput = form.querySelector('input[name="password"]');

    emailInput.addEventListener("input", () => {
        validaEmail(emailInput);
    });

    passwordInput.addEventListener("input", () => {
        validaCampoObbligatorio(
            passwordInput,
            "Inserisci la password."
        );
    });

    form.addEventListener("submit", event => {
        rimuoviMessaggioGenerale(form);

        const emailValida = validaEmail(emailInput);
        const passwordValida = validaCampoObbligatorio(
            passwordInput,
            "Inserisci la password."
        );

        if (!emailValida || !passwordValida) {
            event.preventDefault();

            mostraMessaggioGenerale(
                form,
                "Controlla i campi evidenziati prima di accedere."
            );

            portaAlPrimoErrore(form);
        }
    });
}

function inizializzaRegistrazione(form) {
    const nomeInput = form.querySelector('input[name="nome"]');
    const cognomeInput = form.querySelector('input[name="cognome"]');
    const usernameInput = form.querySelector('input[name="username"]');
    const emailInput = form.querySelector('input[name="email"]');
    const passwordInput = form.querySelector('input[name="password"]');
    const confermaPasswordInput = form.querySelector(
        'input[name="confermaPassword"]'
    );
    const indirizzoInput = form.querySelector('input[name="indirizzo"]');
    const cittaInput = form.querySelector('input[name="citta"]');
    const provinciaInput = form.querySelector('input[name="provincia"]');
    const capInput = form.querySelector('input[name="cap"]');

    aggiungiIndicatorePassword(passwordInput);

    nomeInput.addEventListener("input", () => {
        validaTesto(
            nomeInput,
            2,
            "Il nome deve contenere almeno 2 caratteri."
        );
    });

    cognomeInput.addEventListener("input", () => {
        validaTesto(
            cognomeInput,
            2,
            "Il cognome deve contenere almeno 2 caratteri."
        );
    });

    usernameInput.addEventListener("input", () => {
        validaUsername(usernameInput);
    });

    emailInput.addEventListener("input", () => {
        validaEmail(emailInput);
    });

    passwordInput.addEventListener("input", () => {
        validaPassword(passwordInput);

        if (confermaPasswordInput.value.length > 0) {
            validaConfermaPassword(
                passwordInput,
                confermaPasswordInput
            );
        }

        aggiornaIndicatorePassword(passwordInput);
    });

    confermaPasswordInput.addEventListener("input", () => {
        validaConfermaPassword(
            passwordInput,
            confermaPasswordInput
        );
    });

    indirizzoInput.addEventListener("input", () => {
        validaTesto(
            indirizzoInput,
            5,
            "Inserisci un indirizzo valido."
        );
    });

    cittaInput.addEventListener("input", () => {
        validaTesto(
            cittaInput,
            2,
            "Inserisci una città valida."
        );
    });

    provinciaInput.addEventListener("input", () => {
        provinciaInput.value = provinciaInput.value
            .replace(/[^a-zA-Z]/g, "")
            .toUpperCase()
            .slice(0, 2);

        validaProvincia(provinciaInput);
    });

    capInput.addEventListener("input", () => {
        capInput.value = capInput.value
            .replace(/\D/g, "")
            .slice(0, 5);

        validaCap(capInput);
    });

    form.addEventListener("submit", event => {
        rimuoviMessaggioGenerale(form);

        const controlli = [
            validaTesto(
                nomeInput,
                2,
                "Il nome deve contenere almeno 2 caratteri."
            ),

            validaTesto(
                cognomeInput,
                2,
                "Il cognome deve contenere almeno 2 caratteri."
            ),

            validaUsername(usernameInput),
            validaEmail(emailInput),
            validaPassword(passwordInput),

            validaConfermaPassword(
                passwordInput,
                confermaPasswordInput
            ),

            validaTesto(
                indirizzoInput,
                5,
                "Inserisci un indirizzo valido."
            ),

            validaTesto(
                cittaInput,
                2,
                "Inserisci una città valida."
            ),

            validaProvincia(provinciaInput),
            validaCap(capInput)
        ];

        const formValido = controlli.every(Boolean);

        if (!formValido) {
            event.preventDefault();

            mostraMessaggioGenerale(
                form,
                "Correggi i campi evidenziati prima di registrarti."
            );

            portaAlPrimoErrore(form);
        }
    });
}

function validaCampoObbligatorio(input, messaggio) {
    const valore = input.value.trim();

    if (valore === "") {
        mostraErrore(input, messaggio);
        return false;
    }

    rimuoviErrore(input);
    return true;
}

function validaTesto(input, lunghezzaMinima, messaggio) {
    const valore = input.value.trim();
    const testoRegex = /^[A-Za-zÀ-ÖØ-öø-ÿ' -]+$/;

    if (valore.length < lunghezzaMinima) {
        mostraErrore(input, messaggio);
        return false;
    }

    if (!testoRegex.test(valore)) {
        mostraErrore(
            input,
            "Il campo contiene caratteri non validi."
        );
        return false;
    }

    rimuoviErrore(input);
    return true;
}

function validaUsername(input) {
    const valore = input.value.trim();
    const usernameRegex = /^[a-zA-Z0-9_]{3,20}$/;

    if (!usernameRegex.test(valore)) {
        mostraErrore(
            input,
            "Lo username deve avere da 3 a 20 caratteri e può contenere lettere, numeri e underscore."
        );
        return false;
    }

    rimuoviErrore(input);
    return true;
}

function validaEmail(input) {
    const valore = input.value.trim();
    const emailRegex =
        /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)+$/;

    if (valore === "") {
        mostraErrore(input, "Inserisci l'indirizzo email.");
        return false;
    }

    if (!emailRegex.test(valore)) {
        mostraErrore(input, "Inserisci un indirizzo email valido.");
        return false;
    }

    rimuoviErrore(input);
    return true;
}

function validaPassword(input) {
    const valore = input.value;

    if (valore.length < 8) {
        mostraErrore(
            input,
            "La password deve contenere almeno 8 caratteri."
        );
        return false;
    }

    if (!/[a-z]/.test(valore)) {
        mostraErrore(
            input,
            "La password deve contenere almeno una lettera minuscola."
        );
        return false;
    }

    if (!/[A-Z]/.test(valore)) {
        mostraErrore(
            input,
            "La password deve contenere almeno una lettera maiuscola."
        );
        return false;
    }

    if (!/\d/.test(valore)) {
        mostraErrore(
            input,
            "La password deve contenere almeno un numero."
        );
        return false;
    }

    if (!/[!@#$%^&*(),.?":{}|<>\-_+=/\\[\];']/g.test(valore)) {
        mostraErrore(
            input,
            "La password deve contenere almeno un carattere speciale."
        );
        return false;
    }
    rimuoviErrore(input);
    return true;
}

function validaConfermaPassword(passwordInput, confermaInput) {
    if (confermaInput.value === "") {
        mostraErrore(
            confermaInput,
            "Conferma la password."
        );
        return false;
    }

    if (passwordInput.value !== confermaInput.value) {
        mostraErrore(
            confermaInput,
            "Le password non coincidono."
        );
        return false;
    }

    rimuoviErrore(confermaInput);
    return true;
}

function validaProvincia(input) {
    const valore = input.value.trim().toUpperCase();
    const provinciaRegex = /^[A-Z]{2}$/;

    input.value = valore;

    if (!provinciaRegex.test(valore)) {
        mostraErrore(
            input,
            "La provincia deve essere composta da 2 lettere, ad esempio RM."
        );
        return false;
    }

    rimuoviErrore(input);
    return true;
}

function validaCap(input) {
    const valore = input.value.trim();
    const capRegex = /^\d{5}$/;

    if (!capRegex.test(valore)) {
        mostraErrore(
            input,
            "Il CAP deve essere composto da 5 cifre."
        );
        return false;
    }

    rimuoviErrore(input);
    return true;
}

function mostraErrore(input, messaggio) {
    const inputGroup = input.closest(".input-group");

    input.classList.add("input-error");
    input.setAttribute("aria-invalid", "true");

    let messaggioErrore = inputGroup.querySelector(
        ".field-error"
    );

    if (!messaggioErrore) {
        messaggioErrore = document.createElement("small");
        messaggioErrore.className = "field-error";
        inputGroup.appendChild(messaggioErrore);
    }

    messaggioErrore.textContent = messaggio;
}

function rimuoviErrore(input) {
    const inputGroup = input.closest(".input-group");

    input.classList.remove("input-error");
    input.removeAttribute("aria-invalid");

    const messaggioErrore = inputGroup.querySelector(
        ".field-error"
    );

    if (messaggioErrore) {
        messaggioErrore.remove();
    }
}

function mostraMessaggioGenerale(form, messaggio) {
    let elemento = form.querySelector(".form-error");

    if (!elemento) {
        elemento = document.createElement("div");
        elemento.className = "form-error";
        elemento.setAttribute("role", "alert");

        form.insertBefore(elemento, form.firstChild);
    }

    elemento.textContent = messaggio;
}

function rimuoviMessaggioGenerale(form) {
    const elemento = form.querySelector(".form-error");

    if (elemento) {
        elemento.remove();
    }
}

function portaAlPrimoErrore(form) {
    const primoErrore = form.querySelector(".input-error");

    if (primoErrore) {
        primoErrore.focus();
        primoErrore.scrollIntoView({
            behavior: "smooth",
            block: "center"
        });
    }
}

function aggiungiIndicatorePassword(passwordInput) {
    const inputGroup = passwordInput.closest(".input-group");

    const contenitore = document.createElement("div");
    contenitore.className = "password-strength";

    const barra = document.createElement("div");
    barra.className = "password-strength-bar";

    const testo = document.createElement("small");
    testo.className = "password-strength-text";
    testo.textContent = "Sicurezza della password";

    contenitore.appendChild(barra);
    inputGroup.appendChild(contenitore);
    inputGroup.appendChild(testo);
}

function aggiornaIndicatorePassword(passwordInput) {
    const inputGroup = passwordInput.closest(".input-group");
    const barra = inputGroup.querySelector(
        ".password-strength-bar"
    );
    const testo = inputGroup.querySelector(
        ".password-strength-text"
    );

    if (!barra || !testo) {
        return;
    }

    const password = passwordInput.value;
    let punteggio = 0;

    if (password.length >= 8) {
        punteggio++;
    }

    if (/[a-z]/.test(password) && /[A-Z]/.test(password)) {
        punteggio++;
    }

    if (/\d/.test(password)) {
        punteggio++;
    }

    if (/[!@#$%^&*(),.?":{}|<>\-_+=/\\[\];']/g.test(password)) {
        punteggio++;
    }

    barra.className = "password-strength-bar";

    if (password.length === 0) {
        barra.style.width = "0";
        testo.textContent = "Sicurezza della password";
        return;
    }

    switch (punteggio) {
        case 1:
            barra.classList.add("strength-weak");
            barra.style.width = "25%";
            testo.textContent = "Password debole";
            break;

        case 2:
            barra.classList.add("strength-medium");
            barra.style.width = "50%";
            testo.textContent = "Password media";
            break;

        case 3:
            barra.classList.add("strength-good");
            barra.style.width = "75%";
            testo.textContent = "Password buona";
            break;

        case 4:
            barra.classList.add("strength-strong");
            barra.style.width = "100%";
            testo.textContent = "Password forte";
            break;

        default:
            barra.classList.add("strength-weak");
            barra.style.width = "10%";
            testo.textContent = "Password molto debole";
    }
}
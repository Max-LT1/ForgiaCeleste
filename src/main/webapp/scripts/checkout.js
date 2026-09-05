"use strict";

document.addEventListener("DOMContentLoaded", () => {
    const forms = document.querySelectorAll("form");
    const loginCard = document.getElementById("loginCard");
    const registerCard = document.getElementById("registerCard");
    const checkoutForm = document.getElementById("checkoutForm");

    if (checkoutForm) {
        inizializzaCheckout(checkoutForm);
    }

    if (!loginCard || !registerCard) {
        return false;
    }
});

function inizializzaCheckout(form) {
    const nomeInput = form.querySelector('input[name="nome"]');
    const cognomeInput = form.querySelector('input[name="cognome"]');
    const numeroCartaInput = form.querySelector('input[name="numero"]');
    const meseScadenzaInput = form.querySelector('input[name="mese"]');
    const annoScadenzaInput = form.querySelector('input[name="anno"]');
    const cvvInput = form.querySelector('input[name="cvv"]');


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

    numeroCartaInput.addEventListener("input", () => {
        validaNumero(numeroCartaInput);
    });

    meseScadenzaInput.addEventListener("input", () => {
        validaScadenza(meseScadenzaInput, "MM");
    });

    annoScadenzaInput.addEventListener("input", () => {
        validaScadenza(annoScadenzaInput, "YY");
    });

    cvvInput.addEventListener("input", () => {
        validaCvv(cvvInput);
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

            validaNumero(numeroCartaInput),
            validaScadenza(meseScadenzaInput, "MM"),
            validaScadenza(annoScadenzaInput, "YY"),
            validaCvv(cvvInput)
        ];

        const formValido = controlli.every(Boolean);

        if (!formValido) {
            event.preventDefault();

            mostraMessaggioGenerale(
                form,
                "Correggi i campi evidenziati prima di confermare il pagamento."
            );

            portaAlPrimoErrore(form);
        }
    });
}

function validaTesto(input, lunghezzaMinima, messaggio) {
    const valore = input.value.trim();
    const testoRegex = /^[A-Za-zÀ-ÖØ-öø-ÿ0-9' -]+$/;

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

function validaNumero(input) {
    const valore = input.value.trim();
    const numeroRegex = /^[0-9]{16}$/;

    if (valore === "") {
        mostraErrore(input, "Inserisci il numero della carta.");
        return false;
    }

    if (!numeroRegex.test(valore)) {
        mostraErrore(
            input,
            "Il numero della carta deve essere composto da 16 cifre numeriche"
        );
        return false;
    }

    rimuoviErrore(input);
    return true;
}

function validaScadenza(input, type) {
    const valore = input.value.trim();
    const scadenzaRegex = /^[0-9]{2}$/;

    switch (type){
        case "MM":
            console.log(parseInt(valore));
            if (valore === "") {
                mostraErrore(input, "Inserisci un mese di scadenza valido");
                return false;
            }

            if(parseInt(valore) > 12 || parseInt(valore) < 0){
                mostraErrore(input, "Inserisci un mese di scadenza valido");
                return false;
            }

            if (!scadenzaRegex.test(valore)) {
                mostraErrore(input, "Inserisci un mese di scadenza valido");
                return false;
            }
            break;
        case "YY":
            console.log(parseInt(valore));
            if (valore === "") {
                mostraErrore(input, "Inserisci un anno di scadenza valido");
                return false;
            }

            if (!scadenzaRegex.test(valore)) {
                mostraErrore(input, "Inserisci un anno di scadenza valido");
                return false;
            }
            break;
        default:
            return false;
    }

    rimuoviErrore(input);
    return true;
}

function validaCvv(input) {
    const valore = input.value.trim();
    const scadenzaRegex = /^[0-9]{3}$/;

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
        console.log(messaggio);
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
"use strict";

document.addEventListener("DOMContentLoaded", () => {
    // Configurazione endpoint Backend
    // Cambia questi path se le tue servlet hanno nomi leggermente diversi
    const ENDPOINTS = {
        AGGIORNA_QUANTITA: 'AggiornaCarrello',
        RIMUOVI_PRODOTTO: 'RimuoviDalCarrello',
        SVUOTA_CARRELLO: 'RimuoviDalCarrello'
    };

    const cartItemsContainer = document.getElementById("cartItems");
    const emptyCart = document.getElementById("emptyCart");
    const cartSummary = document.getElementById("cartSummary");
    const cartSubtotal = document.getElementById("cartSubtotal");
    const cartTotal = document.getElementById("cartTotal");
    const cartTotalItems = document.getElementById("cartTotalItems");
    const clearCartButton = document.getElementById("clearCartButton");
    const confirmationModal = document.getElementById("confirmationModal");
    const confirmationTitle = document.getElementById("confirmationTitle");
    const confirmationMessage = document.getElementById("confirmationMessage");
    const cancelConfirmation = document.getElementById("cancelConfirmation");
    const confirmAction = document.getElementById("confirmAction");
    const confirmationBackdrop = confirmationModal?.querySelector(".confirmation-backdrop");

    let azioneConfermata = null;
    let ultimoElementoAttivo = null;

    inizializzaProdotti();
    aggiornaInterfacciaLocali();

    clearCartButton?.addEventListener("click", () => {
        const prodotti = document.querySelectorAll("[data-cart-item]");

        if (prodotti.length === 0) {
            return;
        }

        apriPopup({
            titolo: "Svuota il carrello",
            messaggio: "Vuoi davvero rimuovere tutti i prodotti dal carrello? Questa operazione non può essere annullata.",
            testoConferma: "Svuota carrello",
            azione: svuotaCarrelloServer
        });
    });

    cancelConfirmation?.addEventListener("click", chiudiPopup);
    confirmationBackdrop?.addEventListener("click", chiudiPopup);

    confirmAction?.addEventListener("click", () => {
        if (typeof azioneConfermata === "function") {
            azioneConfermata();
        }
        chiudiPopup();
    });

    document.addEventListener("keydown", event => {
        if (
            event.key === "Escape" &&
            confirmationModal &&
            !confirmationModal.classList.contains("hidden")
        ) {
            chiudiPopup();
        }
    });

    function inizializzaProdotti() {
        const prodotti = document.querySelectorAll("[data-cart-item]");

        prodotti.forEach(prodotto => {
            const decreaseButton = prodotto.querySelector(".decrease-button");
            const increaseButton = prodotto.querySelector(".increase-button");
            const quantityInput = prodotto.querySelector(".quantity-input");
            const removeButton = prodotto.querySelector(".remove-item-button");

            decreaseButton?.addEventListener("click", () => {
                modificaQuantita(prodotto, -1);
            });

            increaseButton?.addEventListener("click", () => {
                modificaQuantita(prodotto, 1);
            });

            quantityInput?.addEventListener("change", () => {
                normalizzaQuantita(quantityInput);
                inviaAggiornamentoServer(prodotto);
            });

            removeButton?.addEventListener("click", () => {
                const nomeProdotto =
                    prodotto.querySelector(".cart-item-name")?.textContent.trim() || "questo prodotto";

                apriPopup({
                    titolo: "Rimuovi prodotto",
                    messaggio: `Vuoi davvero rimuovere “${nomeProdotto}” dal carrello?`,
                    testoConferma: "Rimuovi",
                    azione: () => rimuoviProdottoServer(prodotto)
                });
            });

            aggiornaSubtotaleProdotto(prodotto);
        });
    }

    function modificaQuantita(prodotto, variazione) {
        const input = prodotto.querySelector(".quantity-input");
        if (!input) return;

        const minimo = Number(input.min) || 1;
        const massimo = Number(input.max) || 99;
        const quantitaAttuale = Number(input.value) || minimo;
        const nuovaQuantita = Math.min(massimo, Math.max(minimo, quantitaAttuale + variazione));

        if (nuovaQuantita !== quantitaAttuale) {
            input.value = nuovaQuantita;
            inviaAggiornamentoServer(prodotto);
        }
    }

    // --- COMUNICAZIONE BACKEND ---

    async function inviaAggiornamentoServer(prodotto) {
        const idProdotto = prodotto.dataset.productId;
        const input = prodotto.querySelector(".quantity-input");
        const quantita = Number(input?.value) || 1;

        try {
            const response = await fetch(ENDPOINTS.AGGIORNA_QUANTITA, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ idProdotto: idProdotto, quantita: quantita })
            });

            const data = await response.json();

            if (data.success) {
                aggiornaSubtotaleProdotto(prodotto);
                aggiornaInterfacciaLocali();
                aggiornaBadgeNavbar(data.numeroArticoli);
            } else {
                alert("Errore nell'aggiornamento: " + (data.errore || "Impossibile aggiornare"));
            }
        } catch (error) {
            console.error("Errore durante l'aggiornamento della quantità:", error);
            alert("⚠️ Errore di connessione al server.");
        }
    }

    async function rimuoviProdottoServer(prodotto) {
        const idProdotto = prodotto.dataset.productId;

        try {
            const response = await fetch(ENDPOINTS.RIMUOVI_PRODOTTO, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({azione: "rimuovi", idProdotto: idProdotto })
            });

            const data = await response.json();

            if (data.success) {
                prodotto.classList.add("cart-item-removing");
                window.setTimeout(() => {
                    prodotto.remove();
                    aggiornaInterfacciaLocali();
                    aggiornaBadgeNavbar(data.numeroArticoli);
                }, 300);
            } else {
                alert("Errore nella rimozione: " + (data.errore || "Impossibile rimuovere"));
            }
        } catch (error) {
            console.error("Errore durante la rimozione del prodotto:", error);
            alert("⚠️ Errore di connessione al server.");
        }
    }

    async function svuotaCarrelloServer() {
        try {
            const response = await fetch(ENDPOINTS.SVUOTA_CARRELLO, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({action: "svuota"})
            });

            const data = await response.json();

            if (data.success) {
                const prodotti = document.querySelectorAll("[data-cart-item]");
                prodotti.forEach(p => p.classList.add("cart-item-removing"));

                window.setTimeout(() => {
                    prodotti.forEach(p => p.remove());
                    aggiornaInterfacciaLocali();
                    aggiornaBadgeNavbar(0);
                }, 300);
            } else {
                alert("Errore nello svuotamento del carrello.");
            }
        } catch (error) {
            console.error("Errore durante lo svuotamento:", error);
            alert("⚠️ Errore di connessione al server.");
        }
    }

    // --- FUNZIONI DI CALCOLO E INTERFACCIA LOCALE ---

    function normalizzaQuantita(input) {
        const minimo = Number(input.min) || 1;
        const massimo = Number(input.max) || 99;
        let quantita = parseInt(input.value, 10);

        if (Number.isNaN(quantita)) {
            quantita = minimo;
        }

        input.value = Math.min(massimo, Math.max(minimo, quantita));
    }

    function aggiornaSubtotaleProdotto(prodotto) {
        const prezzo = Number(prodotto.dataset.price) || 0;
        const input = prodotto.querySelector(".quantity-input");
        const subtotalValue = prodotto.querySelector(".subtotal-value");

        if (!input || !subtotalValue) return;

        const quantita = Number(input.value) || 1;
        subtotalValue.textContent = formattaPrezzo(prezzo * quantita);
    }

    function aggiornaInterfacciaLocali() {
        const prodotti = document.querySelectorAll("[data-cart-item]");
        let numeroArticoli = 0;
        let prezzoTotale = 0;

        prodotti.forEach(prodotto => {
            const prezzo = Number(prodotto.dataset.price) || 0;
            const input = prodotto.querySelector(".quantity-input");
            const quantita = Number(input?.value) || 1;

            numeroArticoli += quantita;
            prezzoTotale += prezzo * quantita;
        });

        if (cartTotalItems) cartTotalItems.textContent = numeroArticoli;
        if (cartSubtotal) cartSubtotal.textContent = formattaPrezzo(prezzoTotale);
        if (cartTotal) cartTotal.textContent = formattaPrezzo(prezzoTotale);

        const carrelloVuoto = prodotti.length === 0;

        emptyCart?.classList.toggle("hidden", !carrelloVuoto);
        cartSummary?.classList.toggle("hidden", carrelloVuoto);
    }

    function aggiornaBadgeNavbar(numeroArticoli) {
        const contatori = document.querySelectorAll("#cartCounter");
        contatori.forEach(badge => {
            badge.textContent = numeroArticoli;
            badge.classList.toggle("hidden", numeroArticoli === 0);
        });
    }

    function apriPopup({ titolo, messaggio, testoConferma, azione }) {
        if (!confirmationModal) return;

        ultimoElementoAttivo = document.activeElement;
        azioneConfermata = azione;

        if (confirmationTitle) confirmationTitle.textContent = titolo;
        if (confirmationMessage) confirmationMessage.textContent = messaggio;
        if (confirmAction) confirmAction.textContent = testoConferma;

        confirmationModal.classList.remove("hidden");
        document.body.classList.add("modal-open");

        window.requestAnimationFrame(() => {
            confirmationModal.classList.add("visible");
            confirmAction?.focus();
        });
    }

    function chiudiPopup() {
        if (!confirmationModal) return;

        confirmationModal.classList.remove("visible");

        window.setTimeout(() => {
            confirmationModal.classList.add("hidden");
            document.body.classList.remove("modal-open");
            azioneConfermata = null;

            if (ultimoElementoAttivo && typeof ultimoElementoAttivo.focus === "function") {
                ultimoElementoAttivo.focus();
            }
        }, 200);
    }

    function formattaPrezzo(prezzo) {
        return new Intl.NumberFormat("it-IT", {
            style: "currency",
            currency: "EUR"
        }).format(prezzo);
    }
});
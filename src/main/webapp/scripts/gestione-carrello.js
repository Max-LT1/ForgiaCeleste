function aggiungiAlCarrello(idPianta) {
    // Prepariamo i parametri da inviare (simuliamo un piccolo form invisibile)
    const params = new URLSearchParams();
    params.append('action', 'aggiungi');
    params.append('id', idPianta);

    // Facciamo la chiamata alla Servlet in modo invisibile
    fetch('GestioneCarrello', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params.toString()
    })
        .then(response => response.json()) // Trasformiamo la risposta in un oggetto JavaScript
        .then(data => {
            if (data.success) {
                // Successo! Mostriamo un feedback all'utente
                alert("🌱 Pianta aggiunta con successo!\nTotale articoli nel carrello: " + data.numeroArticoli);

                // BONUS: Se hai un pallino col numero sul carrello nella navbar, si aggiornerebbe così:
                // let badge = document.getElementById('cart-badge');
                // if(badge) badge.innerText = data.numeroArticoli;
            } else {
                // La Servlet ha restituito un errore (es. pianta non trovata)
                alert("⚠️ Errore: " + data.errore);
            }
        })
        .catch(error => {
            console.error('Errore di comunicazione col server:', error);
            alert("⚠️ Errore di comunicazione col server.");
        });
}
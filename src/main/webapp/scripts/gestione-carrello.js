function aggiungiAlCarrello(idProdotto) {
    const params = new URLSearchParams();
    params.append('id', idProdotto);

    fetch('Serv_AggiungiCarrello', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params.toString()
    })
        .then(response => response.json()) // Trasformiamo la risposta in un oggetto JavaScript
        .then(data => {
            if (data.success) {

                alert("Aggiunta con successo: " + data.numeroArticoli);

            } else {

                alert("⚠️ Errore: " + data.errore);
            }
        })
        .catch(error => {
            console.error('Errore di comunicazione col server:', error);
            alert("⚠️ Errore di comunicazione col server.");
        });
}
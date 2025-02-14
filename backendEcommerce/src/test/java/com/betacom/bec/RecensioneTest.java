package com.betacom.bec;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.betacom.bec.dto.RecensioneDTO;
import com.betacom.bec.models.Prodotto;
import com.betacom.bec.models.Recensione;
import com.betacom.bec.models.Utente;
import com.betacom.bec.repositories.ProdottoRepository;
import com.betacom.bec.repositories.RecensioneRepository;
import com.betacom.bec.repositories.UtenteRepository;
import com.betacom.bec.request.RecensioneReq;
import com.betacom.bec.services.interfaces.RecensioneServices;

@SpringBootTest
@ActiveProfiles(value = "sviluppo")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RecensioneTest {

    @Autowired
    private RecensioneServices recensioneS;
    
    @Autowired
    private RecensioneRepository recensioneR;

    @Autowired
    private Logger log;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Test
    @Order(1)
    public void createRecensioneTest() throws Exception {
        // Recupera un utente esistente (o creane uno se necessario)
        Utente utente = utenteRepository.findById(3).orElseThrow(() -> new Exception("Utente non trovato"));

        // Recupera un prodotto esistente (o creane uno se necessario)
        Prodotto prodotto = prodottoRepository.findById(1).orElseThrow(() -> new Exception("Prodotto non trovato"));

        // Crea una richiesta per una nuova recensione
        RecensioneReq req = new RecensioneReq();
        req.setValutazione(5);
        req.setCommento("Ottimo prodotto!");
        req.setDataRecensione("2024/02/12"); // Data in formato corretto
        req.setUtente(utente);
        req.setProdotto(prodotto);

        // Chiama il metodo create
        recensioneS.create(req);
        log.debug("Recensione creata con commento: " + req.getCommento());

        // Recupera tutte le recensioni per verifica
        List<RecensioneDTO> recensioni = recensioneS.listAllRecensioni();

        // Asserzioni
        Assertions.assertThat(recensioni).isNotEmpty();
        Assertions.assertThat(recensioni).anyMatch(rec -> rec.getCommento().equals("Ottimo prodotto!"));
    }

    @Test
    @Order(2)
    public void updateRecensioneTest() throws Exception {
        // Supponiamo che esista una recensione con ID 1
        RecensioneReq req = new RecensioneReq();
        req.setId(1);
        req.setValutazione(4);
        req.setCommento("Buon prodotto ma migliorabile");
        
        // Chiama update
        recensioneS.update(req);
        log.debug("Recensione aggiornata: " + req.getId());

        // Recupera la recensione aggiornata
        Optional<Recensione> recensioneAggiornata = recensioneR.findById(1);

        // Asserzioni
        Assertions.assertThat(recensioneAggiornata).isPresent();
        Assertions.assertThat(recensioneAggiornata.get().getCommento()).isEqualTo("Buon prodotto ma migliorabile");
    }

    @Test
    @Order(3)
    public void listAllRecensioniTest() throws Exception {
        // Chiama il metodo per ottenere tutte le recensioni
        List<RecensioneDTO> recensioni = recensioneS.listAllRecensioni();

        // Asserzioni
        Assertions.assertThat(recensioni).isNotNull();
    }

    @Test
    @Order(4)
    public void listByProdottoTest() {
        // Supponiamo che esista un prodotto con ID 1
        List<RecensioneDTO> recensioni = recensioneS.listByProdotto(1);

        // Asserzioni
        Assertions.assertThat(recensioni).isNotNull();
    }

    @Test
    @Order(5)
    public void deleteRecensioneTest() throws Exception {
        // Supponiamo che esista una recensione con ID 1
        RecensioneReq req = new RecensioneReq();
        req.setId(1);

        // Prima della cancellazione, verifica che la recensione esista
        Optional<Recensione> recPrimaDelete = recensioneR.findById(1);
        Assertions.assertThat(recPrimaDelete).isPresent();

        // Esegui la cancellazione
        recensioneS.removeRecensione(req);
        log.debug("Recensione eliminata: " + req.getId());

        // Dopo la cancellazione, verifica che la recensione non esista più
        Optional<Recensione> recDopoDelete = recensioneR.findById(1);
        Assertions.assertThat(recDopoDelete).isEmpty();
    }

    @Test
    @Order(6)
    public void deleteRecensioneNotFoundTest() {
        // Tentativo di eliminare una recensione con ID non esistente (es. 999)
        RecensioneReq req = new RecensioneReq();
        req.setId(999);

        // Verifica che venga sollevata un'eccezione
        Assertions.assertThatThrownBy(() -> recensioneS.removeRecensione(req))
            .isInstanceOf(Exception.class)
            .hasMessage("no-recensione");
    }
}

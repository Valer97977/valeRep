package com.betacom.bec;

import static org.assertj.core.api.Assertions.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.betacom.bec.dto.OrdineDTO;
import com.betacom.bec.models.Carrello;
import com.betacom.bec.models.Ordine;
import com.betacom.bec.repositories.CarrelloRepository;
import com.betacom.bec.repositories.OrdineRepository;
import com.betacom.bec.request.OrdineReq;
import com.betacom.bec.services.interfaces.OrdineServices;

@SpringBootTest
@ActiveProfiles(value = "sviluppo")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrdineTest {

    @Autowired
    private OrdineServices ordineS;
    
    @Autowired
    private OrdineRepository ordineR;
    
    @Autowired
    private CarrelloRepository carrelloR;
    
    @Autowired
    private Logger log;

    @Test
    @Order(1)
    public void createOrdineTest() throws Exception {
        OrdineReq req = new OrdineReq();
        req.setIndirizzoDiSpedizione("Via Roma, 10");
        req.setCap("00100");
        req.setCitta("Roma");
        req.setDataOrdine("13/02/2024");
        
        // Creazione ordine senza carrello
        ordineS.create(req);
        log.debug("Ordine creato con indirizzo: " + req.getIndirizzoDiSpedizione());

        // Verifica che almeno un ordine sia stato creato
        List<Ordine> ordini = ordineR.findAll();
        assertThat(ordini).isNotEmpty();
    }

    @Test
    @Order(2)
    public void createOrdineWithCarrelloTest() throws Exception {
        Carrello carrello = new Carrello();
        carrello.setQuantita(2);
        carrello.setPrezzo(50.0);
        carrello = carrelloR.save(carrello);

        OrdineReq req = new OrdineReq();
        req.setIndirizzoDiSpedizione("Via Milano, 20");
        req.setCap("20100");
        req.setCitta("Milano");
        req.setCarrelloId(carrello.getId());
        
        ordineS.create(req);
        log.debug("Ordine con carrello creato");

        // Verifica
        List<Ordine> ordini = ordineR.findByCarrelloId(carrello.getId());
        assertThat(ordini).isNotEmpty();
    }
    @Test
    @Order(3)
    public void listByUtenteTest() throws Exception {
        Integer idUtente = 1;
        
        // Verifica che l'utente abbia un carrello (opzionale)
        Optional<Carrello> carrelloOpt = carrelloR.findByUtenteId(idUtente);
        if (carrelloOpt.isPresent()) {
            // Crea un ordine per l'utente se non esiste
            Carrello carrello = carrelloOpt.get();
            
            // Creazione di un ordine associato al carrello
            OrdineReq req = new OrdineReq();
            req.setIndirizzoDiSpedizione("Via Test, 30");
            req.setCap("12345");
            req.setCitta("Test City");
            req.setCarrelloId(carrello.getId());
            
            ordineS.create(req);
            log.debug("Ordine creato per l'utente con ID: " + idUtente);

            // Verifica che ci siano ordini associati all'utente
            List<OrdineDTO> ordini = ordineS.listByUtente(idUtente);
            assertThat(ordini).isNotEmpty();
        } else {
            // Se non esiste un carrello per l'utente, la lista degli ordini dovrebbe essere vuota
            List<OrdineDTO> ordini = ordineS.listByUtente(idUtente);
            assertThat(ordini).isEqualTo(Collections.emptyList());
        }
    }

}

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

import com.betacom.bec.dto.UtenteDTO;
import com.betacom.bec.models.Utente;
import com.betacom.bec.repositories.UtenteRepository;
import com.betacom.bec.request.UtenteReq;
import com.betacom.bec.services.interfaces.UtenteServices;

@SpringBootTest
@ActiveProfiles(value = "sviluppo")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UtenteTest {

    @Autowired
    private UtenteServices utenteS;
    
    @Autowired
    private UtenteRepository utenteR;

    @Autowired
    private Logger log;

    @Test
    @Order(1)
    public void createUtenteTest() throws Exception {
        UtenteReq req = new UtenteReq();
        req.setId(1);
        req.setNome("Mario");
        req.setCognome("Rossi");
        req.setEmail("mario.rossi@example.com");
        req.setPassword("password123");
        req.setRuolo("USER");
        req.setNumeroTelefono("1234567890");
        req.setIndirizzoDiSpedizione("Via Roma, 1");
        req.setIndirizzoDiFatturazione("Via Milano, 2");

        utenteS.create(req);
        log.debug("Utente creato con email: " + req.getEmail());
        
        req.setId(2);
        req.setNome("Marco");
        req.setCognome("Rossi");
        req.setEmail("marco.rossi@example.com");
        req.setPassword("password123");
        req.setRuolo("USER");
        req.setNumeroTelefono("1234567890");
        req.setIndirizzoDiSpedizione("Via Roma, 1");
        req.setIndirizzoDiFatturazione("Via Milano, 2");

        utenteS.create(req);
        log.debug("Utente creato con email: " + req.getEmail());
        
        req.setId(3);
        req.setNome("Marlon");
        req.setCognome("Rossi");
        req.setEmail("marlon.rossi@example.com");
        req.setPassword("password123");
        req.setRuolo("USER");
        req.setNumeroTelefono("1234567890");
        req.setIndirizzoDiSpedizione("Via Roma, 1");
        req.setIndirizzoDiFatturazione("Via Milano, 2");

        utenteS.create(req);
        log.debug("Utente creato con email: " + req.getEmail());

        List<UtenteDTO> utenti = utenteS.list();

        Assertions.assertThat(utenti).isNotEmpty();
        Assertions.assertThat(utenti).anyMatch(u -> u.getEmail().equals(req.getEmail()));
    }

    @Test
    @Order(2)
    public void updateUtenteTest() throws Exception {
        Optional<Utente> uOpt = utenteR.findByNome("Mario");

        Assertions.assertThat(uOpt).isPresent();
        Utente u = uOpt.get();

        UtenteReq req = new UtenteReq();
        req.setId(u.getId());
        req.setCognome("Verdi");
        req.setEmail("mario.verdi@example.com");

        utenteS.update(req);
        log.debug("Utente aggiornato con ID: " + req.getId());

        Utente utenteAggiornato = utenteR.findById(u.getId())
                .orElseThrow(() -> new Exception("Utente non trovato"));

        Assertions.assertThat(utenteAggiornato.getCognome()).isEqualTo("Verdi");
        Assertions.assertThat(utenteAggiornato.getEmail()).isEqualTo("mario.verdi@example.com");
    }

    @Test
    @Order(3)
    public void listAllUtentiTest() {
        List<UtenteDTO> utenti = utenteS.list();

        Assertions.assertThat(utenti).isNotNull();
        //Assertions.assertThat(utenti).isNotEmpty();
    }

    @Test
    @Order(4)
    public void removeUtenteTest() throws Exception {
        Optional<Utente> uOpt = utenteR.findByNome("Marco");

        Assertions.assertThat(uOpt).isPresent();
        Utente u = uOpt.get();

        UtenteReq req = new UtenteReq();
        req.setId(u.getId());

        utenteS.remove(req);
        log.debug("Utente eliminato con ID: " + req.getId());

        Optional<Utente> utenteDopoDelete = utenteR.findById(u.getId());
        Assertions.assertThat(utenteDopoDelete).isEmpty();
    }

    @Test
    @Order(5)
    public void removeUtenteNotFoundTest() {
        UtenteReq req = new UtenteReq();
        req.setId(9999); // ID inesistente

        Assertions.assertThatThrownBy(() -> utenteS.remove(req))
                .isInstanceOf(Exception.class)
                .hasMessage("Username inesistente");
    }
}

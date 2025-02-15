package com.betacom.bec;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.betacom.bec.controller.CarrelloController;
import com.betacom.bec.models.Carrello;
import com.betacom.bec.response.ResponseBase;
import com.betacom.bec.response.ResponseList;
import com.betacom.bec.response.ResponseObject;
import com.betacom.bec.dto.CarrelloDTO;

@SpringBootTest
@ActiveProfiles(value = "sviluppo")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CarrelloControllerTest {

    @Autowired
    CarrelloController carrelloController;

    @Autowired
    Logger log;

    @Test
    @Order(1)
    public void getAllCarrelliTest() {
        int idUtente = 1; // Supponiamo che l'utente con ID 1 esista
        ResponseList<CarrelloDTO> response = carrelloController.getAllCarrelli(idUtente);

        Assertions.assertThat(response.getRc()).isEqualTo(true);
        //Assertions.assertThat(response.getDati()).isNotEmpty();
        log.debug("Carrello ottenuto per utente ID: " + idUtente);
    }

    @Test
    @Order(2)
    public void aggiungiProdottoTest() {
        int utenteId = 1;
        int prodottoId = 10;
        int quantita = 2;

        ResponseObject<Carrello> response = carrelloController.aggiungiProdotto(utenteId, prodottoId, quantita);

        Assertions.assertThat(response.getRc()).isEqualTo(false);
        //Assertions.assertThat(response.getDati()).isNotNull();
        log.debug("Prodotto aggiunto al carrello per utente ID: " + utenteId);
    }

    @Test
    @Order(3)
    public void rimuoviProdottoTest() {
        int utenteId = 1;
        int prodottoId = 10;
        int quantitaDaRimuovere = 1;

        ResponseBase response = carrelloController.rimuoviProdotto(utenteId, prodottoId, quantitaDaRimuovere);

        Assertions.assertThat(response.getRc()).isEqualTo(false);
        log.debug("Prodotto rimosso dal carrello per utente ID: " + utenteId);
    }

    @Test
    @Order(4)
    public void aggiungiProdottoCarrelloInesistenteTest() {
        int utenteId = 999; // Utente inesistente
        int prodottoId = 10;
        int quantita = 1;

        ResponseObject<Carrello> response = carrelloController.aggiungiProdotto(utenteId, prodottoId, quantita);

        Assertions.assertThat(response.getRc()).isEqualTo(false);
        Assertions.assertThat(response.getMsg()).contains("Carrello non trovato per l'utente");
    }

    @Test
    @Order(5)
    public void aggiungiProdottoNonEsistenteTest() {
        int utenteId = 1;
        int prodottoId = 999; // Prodotto inesistente
        int quantita = 1;

        ResponseObject<Carrello> response = carrelloController.aggiungiProdotto(utenteId, prodottoId, quantita);

        Assertions.assertThat(response.getRc()).isEqualTo(false);
        Assertions.assertThat(response.getMsg()).contains("Prodotto con ID 999 non trovato.");
    }

    @Test
    @Order(6)
    public void rimuoviProdottoNonPresenteTest() {
        int utenteId = 1;
        int prodottoId = 999; // Prodotto non presente nel carrello
        int quantita = 1;

        ResponseBase response = carrelloController.rimuoviProdotto(utenteId, prodottoId, quantita);

        Assertions.assertThat(response.getRc()).isEqualTo(false);
        Assertions.assertThat(response.getMsg()).contains("Prodotto con ID 999 non trovato.");
    }
}

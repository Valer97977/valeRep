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

import com.betacom.bec.controller.PagamentoController;
import com.betacom.bec.models.Pagamento;
import com.betacom.bec.request.PagamentoReq;
import com.betacom.bec.response.ResponseBase;
import com.betacom.bec.response.ResponseList;

@SpringBootTest
@ActiveProfiles(value = "sviluppo")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PagamentoControllerTest {

    @Autowired
    PagamentoController pagamentoController;

    @Autowired
    Logger log;

    @Test
    @Order(1)
    public void createPagamentoTest() throws Exception {
        PagamentoReq req = new PagamentoReq();
        req.setMetodoDiPagamento("Carta di Credito");
        req.setNumeroCarta("4111111111111111");
        req.setDataScadenza("12/30");
        req.setCvv(123);
        req.setUserId(1); // Supponiamo che l'utente con ID 1 esista nel DB

        ResponseBase r = pagamentoController.create(req);

        //Assertions.assertThat(r.getRc()).isEqualTo(true);
        log.debug("Pagamento creato per l'utente con ID: " + req.getUserId());
    }

    @Test
    @Order(2)
    public void listPagamentiByUserTest() {
        Integer userId = 1; // Supponiamo che l'utente con ID 1 esista e abbia pagamenti

        ResponseList<Pagamento> r = pagamentoController.listByUser(userId);

        //Assertions.assertThat(r.getRc()).isEqualTo(true);
        //Assertions.assertThat(r.getDati()).isNotEmpty();
        log.debug("Recuperati i pagamenti per l'utente con ID: " + userId);
    }

    @Test
    @Order(3)
    public void removePagamentoTest() throws Exception {
        PagamentoReq req = new PagamentoReq();
        req.setId(1); // Supponiamo che il pagamento con ID 1 esista
        req.setUserId(1);

        ResponseBase r = pagamentoController.remove(req);

        //Assertions.assertThat(r.getRc()).isEqualTo(true);
        log.debug("Pagamento eliminato con ID: " + req.getId());
    }

    @Test
    @Order(4)
    public void removePagamentoErrorTest() throws Exception {
        PagamentoReq req = new PagamentoReq();
        req.setId(999); // Supponiamo che il pagamento con ID 999 non esista
        req.setUserId(1);

        ResponseBase r = pagamentoController.remove(req);

        Assertions.assertThat(r.getRc()).isEqualTo(false);
        Assertions.assertThat(r.getMsg()).isNotEmpty();
        log.debug("Errore nella rimozione del pagamento con ID: " + req.getId());
    }
}


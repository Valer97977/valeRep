package com.betacom.bec;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import com.betacom.jpa.controllers.ProdottoController;
import com.betacom.jpa.dto.ProdottoDTO;
import com.betacom.jpa.request.ProdottoReq;
import com.betacom.jpa.response.ResponseBase;
import com.betacom.jpa.response.ResponseList;
import com.betacom.jpa.response.ResponseObject;

@SpringBootTest
public class ProdottoControllerTest {

    @Autowired
    ProdottoController prodottoController;

    @Autowired
    Logger log;

    // Test del metodo list
    @Test
    @Order(2)
    public void listProdottoTest() {
        ResponseList<ProdottoDTO> r = prodottoController.list();

        Assertions.assertThat(r.getRc()).isEqualTo(true);
        Assertions.assertThat(r.getDati().get(0).getNome()).isEqualTo("ProdottoA");
    }

    // Test del metodo getById
    @Test
    @Order(3)
    public void getByIdTest() {
        ResponseObject<ProdottoDTO> r = prodottoController.getById("Prod-1");

        Assertions.assertThat(r.getRc()).isEqualTo(true);
        Assertions.assertThat(r.getDati().getNome()).isEqualTo("ProdottoA");
    }

    // Test per la gestione degli errori con getById
    @Test
    @Order(4)
    public void getByIdErrorTest() {
        ResponseObject<ProdottoDTO> r = prodottoController.getById("Prod-4");
        Assertions.assertThat(r.getRc()).isEqualTo(false);
        Assertions.assertThat(r.getMsg()).isEqualTo("Prodotto non trovato");
    }

    // Test del metodo create
    @Test
    @Order(1)
    public void createProdottoTest() throws Exception {

        ProdottoReq req = new ProdottoReq();
        req.setIdProdotto("Prod-1");
        req.setNome("ProdottoA");

        ResponseBase response = prodottoController.create(req);
        log.debug("Prodotto creato: " + req);

        req.setIdProdotto("Prod-2");
        req.setNome("ProdottoB");

        response = prodottoController.create(req);
        log.debug("Prodotto creato: " + req);
    }

    // Test del metodo update
    @Test
    @Order(5)
    public void updateProdottoTest() throws Exception {

        ProdottoReq req = new ProdottoReq();
        req.setIdProdotto("Prod-2");
        req.setNome("ProdottoB_updated");

        ResponseBase response = prodottoController.update(req);
        log.debug("Update riuscito: " + req.toString());
    }

    // Test del metodo delete
    @Test
    @Order(6)
    public void deleteProdottoTest() throws Exception {

        ProdottoReq req = new ProdottoReq();
        req.setIdProdotto("Prod-2");

        ResponseBase response = prodottoController.delete(req);
        log.debug("Prodotto eliminato: " + req.toString());
    }
}

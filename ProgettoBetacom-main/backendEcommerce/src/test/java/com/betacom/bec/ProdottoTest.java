package com.betacom.bec;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.betacom.jpa.dto.ProdottoDTO;
import com.betacom.jpa.request.ProdottoReq;
import com.betacom.jpa.services.interfaces.ProdottoServices;

@SpringBootTest
@ActiveProfiles(value = "sviluppo")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProdottoTest {

    @Autowired
    ProdottoServices prodottoService;

    @Autowired
    Logger log;

    @Test
    @Order(1)
    public void createProdottoTest() throws Exception {

        ProdottoReq req = new ProdottoReq();
        req.setIdProdotto("Prod-1");
        req.setNome("ProdottoA");

        prodottoService.create(req);
        log.debug("Prodotto creato: " + req);

        req.setIdProdotto("Prod-2");
        req.setNome("ProdottoB");

        prodottoService.create(req);
        log.debug("Prodotto creato: " + req);

        List<ProdottoDTO> lP = prodottoService.listAll();
        Assertions.assertThat(lP.size()).isLessThanOrEqualTo(3);
    }

    @Test
    @Order(2)
    public void updateProdottoTest() throws Exception {

        ProdottoReq req = new ProdottoReq();
        req.setIdProdotto("Prod-2");
        req.setNome("ProdottoB_updated");

        prodottoService.update(req);
        log.debug("Prodotto aggiornato: " + req.toString());
    }

    @Test
    @Order(3)
    public void deleteProdottoTest() throws Exception {

        ProdottoReq req = new ProdottoReq();
        req.setIdProdotto("Prod-2");

        prodottoService.delete(req);

        List<ProdottoDTO> prodottoList = prodottoService.listAll();
        Assertions.assertThat(prodottoList.size()).isEqualTo(1);
    }

    @Test
    @Order(4)
    public void listAllProdottiTest() throws Exception {

        List<ProdottoDTO> prodotti = prodottoService.listAll();
        Assertions.assertThat(prodotti).isNotEmpty();
        log.debug("Totale prodotti presenti: " + prodotti.size());
    }

    @Test
    @Order(5)
    public void findByIdProdottoTest() throws Exception {

        ProdottoDTO prodotto = prodottoService.findById("Prod-1");

        Assertions.assertThat(prodotto).isNotNull();
        Assertions.assertThat(prodotto.getIdProdotto()).isEqualTo("Prod-1");
        Assertions.assertThat(prodotto.getNome()).isEqualTo("ProdottoA");
        log.debug("Prodotto trovato con ID 1: " + prodotto.getIdProdotto());

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            prodottoService.findById("Prod-999"); // ID che non esiste
        });

        Assertions.assertThat(exception).isInstanceOf(Exception.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo("Prodotto inesistente");
    }
}

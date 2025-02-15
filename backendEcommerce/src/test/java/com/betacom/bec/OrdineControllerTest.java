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

import com.betacom.bec.controller.OrdineController;
import com.betacom.bec.dto.OrdineDTO;
import com.betacom.bec.request.OrdineReq;
import com.betacom.bec.response.ResponseBase;
import com.betacom.bec.response.ResponseList;

@SpringBootTest
@ActiveProfiles(value = "sviluppo")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrdineControllerTest {

    @Autowired
    OrdineController ordineController;

    @Autowired
    Logger log;

    @Test
    @Order(1)
    public void createOrdineTest() throws Exception {
        OrdineReq req = new OrdineReq();
        req.setIndirizzoDiSpedizione("Via Roma, 10");
        req.setCap("00100");
        req.setCitta("Roma");
        req.setDataOrdine("2024-02-13");
        req.setCarrelloId(1);

        ResponseBase r = ordineController.create(req);
        
        //Assertions.assertThat(r.getRc()).isEqualTo(true);
        log.debug("Ordine creato con indirizzo: " + req.getIndirizzoDiSpedizione());
    }

    @Test
    @Order(2)
    public void listByUtenteTest() {
        ResponseList<OrdineDTO> r = ordineController.listByUtente(1);

        Assertions.assertThat(r.getRc()).isEqualTo(true);
        //Assertions.assertThat(r.getDati()).isNotEmpty();
    }

    @Test
    @Order(3)
    public void createOrdineErrorTest() throws Exception {
        OrdineReq req = new OrdineReq();
        req.setCap("00100");
        req.setCitta("Roma");

        ResponseBase r = ordineController.create(req);
        
        Assertions.assertThat(r.getRc()).isEqualTo(false);
        Assertions.assertThat(r.getMsg()).isNotEmpty();
    }
}

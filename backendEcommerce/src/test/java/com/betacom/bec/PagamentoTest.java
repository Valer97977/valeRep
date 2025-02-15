package com.betacom.bec;

import static org.assertj.core.api.Assertions.*;

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

import com.betacom.bec.models.Pagamento;
import com.betacom.bec.repositories.PagamentoRepository;
import com.betacom.bec.request.PagamentoReq;
import com.betacom.bec.services.interfaces.PagamentoServices;

@SpringBootTest
@ActiveProfiles(value = "sviluppo")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PagamentoTest {

    @Autowired
    private PagamentoServices pagamentoS;
    
    @Autowired
    private PagamentoRepository pagamentoR;

    @Autowired
    private Logger log;

    @Test
    @Order(1)
    public void createPagamentoTest() throws Exception {
        PagamentoReq req = new PagamentoReq();
        req.setMetodoDiPagamento("Carta di Credito");
        req.setNumeroCarta("1234567812345678");
        req.setDataScadenza("12/30");
        req.setCvv(123);
        req.setUserId(1);

        pagamentoS.create(req);
        log.debug("Pagamento creato con numero carta: " + req.getNumeroCarta());

        List<Pagamento> pagamenti = pagamentoS.getPagamentiByUserId(1);
        assertThat(pagamenti).isNotEmpty();
        assertThat(pagamenti).anyMatch(p -> p.getNumeroCarta().equals("1234567812345678"));
    }

    @Test
    @Order(2)
    public void isNumeroCartaValidoTest() throws Exception {
        boolean valido = pagamentoS.isNumeroCartaValido("1234567812345678");
        boolean nonValido = pagamentoS.isNumeroCartaValido("1234");

        assertThat(valido).isTrue();
        assertThat(nonValido).isFalse();
    }

    @Test
    @Order(3)
    public void isDataScadenzaValidaTest() {
        boolean valida = pagamentoS.isDataScadenzaValida("12/30");
        boolean nonValida = pagamentoS.isDataScadenzaValida("01/20");

        assertThat(valida).isTrue();
        assertThat(nonValida).isFalse();
    }

    @Test
    @Order(4)
    public void getPagamentiByUserIdTest() {
        List<Pagamento> pagamenti = pagamentoS.getPagamentiByUserId(1);
        assertThat(pagamenti).isNotNull();
        assertThat(pagamenti).isNotEmpty();
    }

    @Test
    @Order(5)
    public void removePagamentoTest() throws Exception {
        Optional<Pagamento> pagamentoOpt = pagamentoR.findAll().stream().findFirst();
        assertThat(pagamentoOpt).isPresent();
        
        PagamentoReq req = new PagamentoReq();
        req.setId(pagamentoOpt.get().getId());
        req.setUserId(1);

        pagamentoS.removePagamento(req);
        
        Optional<Pagamento> deletedPagamento = pagamentoR.findById(req.getId());
        assertThat(deletedPagamento).isEmpty();
    }
}
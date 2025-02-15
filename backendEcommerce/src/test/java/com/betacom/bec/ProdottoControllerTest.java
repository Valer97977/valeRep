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

import com.betacom.bec.controller.ProdottoController;
import com.betacom.bec.dto.ProdottoDTO;
import com.betacom.bec.request.ProdottoReq;
import com.betacom.bec.response.ResponseBase;
import com.betacom.bec.response.ResponseList;

@SpringBootTest
@ActiveProfiles(value = "sviluppo")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ProdottoControllerTest {

	@Autowired
	ProdottoController prodottoController;

	@Autowired
	Logger log;

	@Test
	@Order(2)
	public void listByCategoriaTest() {
		ResponseList<ProdottoDTO> r = prodottoController.listByCategoria("Categoria Test");

		//Assertions.assertThat(r.getRc()).isEqualTo(false);
		//Assertions.assertThat(r.getDati()).isNotEmpty();
	}

	@Test
	@Order(1)
	public void createProdottoTest() throws Exception {
		ProdottoReq req1 = new ProdottoReq();
		req1.setNome("Prodotto Test");
		req1.setMarca("Marca Test");
		req1.setCategoria("Categoria Test");
		req1.setDescrizione("Descrizione Test");
		req1.setPrezzo(100.0);
		req1.setquantitaDisponibile(10);
		req1.setUrlImg("http://url.com");
		req1.setSize("L");
		req1.setColore("Blu");

		ResponseBase r = prodottoController.create(req1);

		//Assertions.assertThat(r.getRc()).isEqualTo(false);
		log.debug("Prodotto creato: " + req1.getNome());
	}

	@Test
	@Order(3)
	public void updateProdottoTest() throws Exception {
		ProdottoReq req = new ProdottoReq();
		req.setId(1); // Supponiamo che il prodotto con ID 1 esista
		req.setNome("Prodotto Aggiornato");
		req.setPrezzo(120.0);
		req.setquantitaDisponibile(20);

		ResponseBase r = prodottoController.update(req);

		//Assertions.assertThat(r.getRc()).isEqualTo(false);
		log.debug("Prodotto aggiornato con nome: " + req.getNome());
	}

	@Test
	@Order(4)
	public void removeProdottoTest() throws Exception {
		ProdottoReq req = new ProdottoReq();
		req.setId(1); // Supponiamo che il prodotto con ID 1 esista

		ResponseBase r = prodottoController.remove(req);

		//Assertions.assertThat(r.getRc()).isEqualTo(false);
		log.debug("Prodotto eliminato con ID: " + req.getId());
	}

	@Test
	@Order(5)
	public void removeProdottoErrorTest() throws Exception {
		ProdottoReq req = new ProdottoReq();
		req.setId(999); // ID che non esiste

		ResponseBase r = prodottoController.remove(req);

		Assertions.assertThat(r.getRc()).isEqualTo(false);
		Assertions.assertThat(r.getMsg()).isEqualTo("no-prodotto");
	}
}
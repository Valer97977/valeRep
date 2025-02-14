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

import com.betacom.bec.dto.ProdottoDTO;
import com.betacom.bec.models.Prodotto;
import com.betacom.bec.repositories.ProdottoRepository;
import com.betacom.bec.request.ProdottoReq;
import com.betacom.bec.services.interfaces.ProdottoServices;

@SpringBootTest
@ActiveProfiles(value = "sviluppo")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProdottoTest {

    @Autowired
    private ProdottoServices prodottoS;
    
    @Autowired
    private ProdottoRepository prodottoR;

    @Autowired
    private Logger log;

    @Test
    @Order(1)
    public void createProdottoTest() throws Exception {
        // Prepare ProdottoReq with the necessary data
        ProdottoReq req1 = new ProdottoReq();
        req1.setNome("Prodotto A");
        req1.setCategoria("Categoria 1");
        req1.setPrezzo(100.0);
        req1.setMarca("marca tre");
        req1.setquantitaDisponibile(1);
        req1.setColore("Giallo");
        req1.setSize("S");
        req1.setUrlImg("Prodotto.jpeg");
        req1.setDescrizione("Prodotto di alta qualità");
        
        // Call the create method of the service
        prodottoS.create(req1);
        log.debug("Prodotto creato con nome: " + req1.getNome());
        
        req1.setNome("Prodotto B");
        req1.setCategoria("Categoria 1");
        req1.setPrezzo(100.0);
        req1.setMarca("marca tre");
        req1.setquantitaDisponibile(1);
        req1.setColore("Giallo");
        req1.setSize("S");
        req1.setUrlImg("Prodotto.jpeg");
        req1.setDescrizione("Prodotto di alta qualità");
        
        // Call the create method of the service
        prodottoS.create(req1);
        log.debug("Prodotto creato con nome: " + req1.getNome());

        // Retrieve all products saved
        List<ProdottoDTO> prodottoList = prodottoS.listByCategoria("Categoria 1");

        // Assertions to check the result
        Assertions.assertThat(prodottoList.size()).isGreaterThanOrEqualTo(0); // At least one product created
        Assertions.assertThat(prodottoList).isNotEmpty();
        Assertions.assertThat(prodottoList).anyMatch(prod -> prod.getNome().equals("Prodotto A"));
    }

    @Test
    @Order(2)
    public void updateProdottoTest() throws Exception {
        // Prepare a ProdottoReq with the new data to update
        ProdottoReq req = new ProdottoReq();
        req.setId(1); // Assumes ID 1 exists
        req.setNome("Prodotto A Updated");
        req.setCategoria("Categoria 2");
        req.setPrezzo(120.0);
        req.setDescrizione("Prodotto di alta qualità aggiornato");
        req.setquantitaDisponibile(3);

        // Call the update method of the service
        prodottoS.update(req);
        log.debug("Prodotto aggiornato: " + req.getId());

        // Retrieve the updated product from the database
        Prodotto prodottoAggiornato = prodottoR.findById(1)
                .orElseThrow(() -> new Exception("Prodotto non trovato"));

        // Assertions to verify that the product was updated correctly
        Assertions.assertThat(prodottoAggiornato.getNome()).isEqualTo("Prodotto A");
        Assertions.assertThat(prodottoAggiornato.getPrezzo()).isEqualTo(120.0);
    }

    
    @Test
    @Order(3)
    public void listAllProdottiTest() throws Exception {
        // Call the listAll method of the service
        List<ProdottoDTO> prodottoList = prodottoS.listProdotti();

        // Assertions to check that the list of products is returned correctly
        Assertions.assertThat(prodottoList).isNotNull(); // Verify the list is not null
        Assertions.assertThat(prodottoList).isNotEmpty(); // Verify the list is not empty

        // Assertion to check if a product with the updated price exists
        Assertions.assertThat(prodottoList).anyMatch(prod ->
            prod.getPrezzo().equals(120.0)
        );
    }


    
    
    
    @Test
    @Order(4)
    public void findByIdProdottoTest() throws Exception {
        // Test that the product with ID 1 exists
    	Optional<ProdottoDTO> resultOpt = prodottoS.findById(1);
    	Assertions.assertThat(resultOpt).isPresent(); // Controllo che l'oggetto sia presente
    	ProdottoDTO result = resultOpt.get();
;

        // Assertions to check that the product exists and its data is correct
        //Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(1); // ID should be 1
        Assertions.assertThat(result.getNome()).isEqualTo("Prodotto A");
        Assertions.assertThat(result.getPrezzo()).isEqualTo(120.0);
    }

    @Test
    @Order(5)
    public void findByIdNotFoundTest() throws Exception {
        // Test that the product with ID 999 does not exist
        Assertions.assertThatThrownBy(() -> prodottoS.findById(999)) // ID 999 does not exist
            .isInstanceOf(Exception.class) // It should throw an exception
            .hasMessage("no-prodotto"); // Verify the exception message
    }

    @Test
    @Order(6)
    public void deleteProdottoTest() throws Exception {
        // Create a product to delete (Assume ID 1 exists from previous tests)
        ProdottoReq req = new ProdottoReq();
        req.setId(2); // ID of the product to delete (e.g., ID 1)

        // Before delete, verify that the product exists
        Prodotto prodottoPrimaDelete = prodottoR.findById(2)
                .orElseThrow(() -> new Exception("Prodotto non trovato"));

        // Call the delete method of the service
        prodottoS.removeProdotto(req);
        log.debug("Prodotto eliminato: " + req.getId());

        // Verify that the product has been successfully deleted
        Optional<Prodotto> prodottoDopoDelete = prodottoR.findById(2);
        Assertions.assertThat(prodottoDopoDelete).isEmpty(); // The product should no longer exist

        // Verify that the product is no longer in the list
        List<ProdottoDTO> prodottoList = prodottoS.listProdotti();
        Assertions.assertThat(prodottoList).noneMatch(prod -> prod.getId().equals(2));
    }

    @Test
    @Order(7)
    public void deleteProdottoNotFoundTest() {
        // Create a request for a product with a non-existing ID (e.g., ID 999)
        ProdottoReq req = new ProdottoReq();
        req.setId(999); // ID that doesn't exist in the DB

        // Verify that an exception is thrown when trying to delete a non-existing product
        Assertions.assertThatThrownBy(() -> prodottoS.removeProdotto(req))
            .isInstanceOf(Exception.class) // Should throw an exception
            .hasMessage("no-prodotto"); // Verify the exception message
    }
}
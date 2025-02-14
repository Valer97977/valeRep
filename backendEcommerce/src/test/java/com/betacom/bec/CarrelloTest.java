package com.betacom.bec;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.betacom.bec.dto.CarrelloDTO;
import com.betacom.bec.models.Carrello;
import com.betacom.bec.models.Prodotto;
import com.betacom.bec.models.Utente;
import com.betacom.bec.repositories.CarrelloRepository;
import com.betacom.bec.repositories.ProdottoRepository;
import com.betacom.bec.repositories.UtenteRepository;
import com.betacom.bec.services.interfaces.CarrelloServices;

@SpringBootTest
@ActiveProfiles(value = "sviluppo")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CarrelloTest {

    @Autowired
    private CarrelloServices carrelloService;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;
    
    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private Logger log;
  
    @BeforeEach
    public void setup() {
        // Crea un utente di test e imposta tutti i campi obbligatori
        Utente utente = new Utente();
        utente.setNome("NomeUtente");
        utente.setCognome("CognomeUtente");
        utente.setEmail("utente@example.com");
        utente.setPsw("hashedPassword");
        utente.setNumeroTelefono("1234567890");
        utente.setIndirizzoDiSpedizione("Via Spedizione, 1");
        utente.setIndirizzoDiFatturazione("Via Fatturazione, 1");
        
        // Salva l'utente
        utenteRepository.save(utente);

        // Crea un carrello per l'utente
        Carrello carrello = new Carrello();
        carrello.setUtente(utente);
        
        // Salva il carrello
        carrelloRepository.save(carrello);
    }

    @Test
    @Order(1)
    public void aggiungiProdottoAlCarrelloTest() {
        Integer utenteId = 1;
        Integer prodottoId = 1;  // Assicurati che il prodotto con questo ID esista nel DB di test
        Integer quantita = 2;

        Carrello carrello = carrelloService.aggiungiProdottoAlCarrello(utenteId, prodottoId, quantita);
        log.debug("Prodotto aggiunto al carrello, ID carrello: " + carrello.getId());

        Assertions.assertThat(carrello).isNotNull();
        Assertions.assertThat(carrello.getCarrelloProdotti()).isNotEmpty();
        Assertions.assertThat(carrello.getCarrelloProdotti().get(0).getQuantita()).isEqualTo(quantita);  // Assicurati che la quantità sia corretta
    }

    @Test
    @Order(2)
    public void rimuoviProdottoDalCarrelloTest() {
        Integer utenteId = 1;
        Integer prodottoId = 1;  // Assicurati che il prodotto con questo ID esista nel DB di test
        Integer quantitaDaRimuovere = 1;

        Carrello carrello = carrelloService.rimuoviProdotto(utenteId, prodottoId, quantitaDaRimuovere);
        log.debug("Prodotto rimosso dal carrello, ID carrello: " + carrello.getId());

        Assertions.assertThat(carrello).isNotNull();
        Assertions.assertThat(carrello.getCarrelloProdotti()).allMatch(cp -> cp.getProdotto().getId() != prodottoId || cp.getQuantita() >= 0);
    }

    @Test
    @Order(3)
    public void ottieniCarrelloTest() {
        Integer utenteId = 1;
        List<CarrelloDTO> carrelloDTOList = carrelloService.ottieniCarrello(utenteId);

        log.debug("Carrello ottenuto per utente " + utenteId);

        Assertions.assertThat(carrelloDTOList).isNotNull();
    }

    @Test
    @Order(4)
    public void aggiungiProdottoCarrelloNonEsistenteTest() {
        Integer utenteId = 999;  // ID utente non esistente
        Integer prodottoId = 1;
        Integer quantita = 1;

        Assertions.assertThatThrownBy(() -> carrelloService.aggiungiProdottoAlCarrello(utenteId, prodottoId, quantita))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Carrello non trovato per l'utente 999");
    }

    @Test
    @Order(5)
    public void aggiungiProdottoNonEsistenteTest() {
        Integer utenteId = 1;
        Integer prodottoId = 999;  // ID prodotto non esistente
        Integer quantita = 1;

        Assertions.assertThatThrownBy(() -> carrelloService.aggiungiProdottoAlCarrello(utenteId, prodottoId, quantita))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Prodotto non trovato con ID 999");
    }

    @Test
    @Order(6)
    public void rimuoviProdottoNonPresenteTest() {
        Integer utenteId = 1;
        Integer prodottoId = 999;  // ID prodotto non esistente
        Integer quantita = 1;

        Assertions.assertThatThrownBy(() -> carrelloService.rimuoviProdotto(utenteId, prodottoId, quantita))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Prodotto non trovato con ID 999");
    }
}

package com.betacom.bec.services.implementations;

import com.betacom.bec.dto.ProdottoDTO;
import com.betacom.bec.models.Prodotto;
import com.betacom.bec.repositories.ProdottoRepository;
import com.betacom.bec.request.ProdottoReq;
import com.betacom.bec.services.interfaces.MessaggioServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdottoImplTest {

    @InjectMocks
    private ProdottoImpl prodottoService;

    @Mock
    private ProdottoRepository prodottoRepository;

    @Mock
    private MessaggioServices messaggioServices;

    private ProdottoReq prodottoReq;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        prodottoReq = new ProdottoReq();
        prodottoReq.setNome("Prodotto 1");
        prodottoReq.setMarca("Marca A");
        prodottoReq.setCategoria("Categoria 1");
        prodottoReq.setDescrizione("Descrizione prodotto");
        prodottoReq.setPrezzo(100.0);
        prodottoReq.setquantitaDisponibile(10);
        prodottoReq.setUrlImg("url.com");
        prodottoReq.setSize("M");
        prodottoReq.setColore("Rosso");
    }

    @Test
    void createProdotto_ShouldThrowException_WhenProductAlreadyExists() throws Exception {
        when(prodottoRepository.findByNome(prodottoReq.getNome().trim())).thenReturn(java.util.Optional.of(new Prodotto()));

        Exception exception = assertThrows(Exception.class, () -> prodottoService.create(prodottoReq));

        assertEquals("Prodotto già presente", exception.getMessage());
    }

    @Test
    void createProdotto_ShouldSaveProduct() throws Exception {
        when(prodottoRepository.findByNome(prodottoReq.getNome().trim())).thenReturn(java.util.Optional.empty());
        Prodotto prodotto = new Prodotto();
        prodotto.setNome(prodottoReq.getNome());

        when(prodottoRepository.save(any(Prodotto.class))).thenReturn(prodotto);

        prodottoService.create(prodottoReq);

        verify(prodottoRepository, times(1)).save(any(Prodotto.class));
    }

    @Test
    void updateProdotto_ShouldThrowException_WhenProductNotFound() throws Exception {
        when(prodottoRepository.findById(prodottoReq.getId())).thenReturn(java.util.Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> prodottoService.update(prodottoReq));

        assertEquals("Prodotto non trovato", exception.getMessage());
    }

    @Test
    void updateProdotto_ShouldUpdateProduct() throws Exception {
        Prodotto existingProdotto = new Prodotto();
        existingProdotto.setId(1);
        existingProdotto.setNome("Prodotto 1");

        when(prodottoRepository.findById(prodottoReq.getId())).thenReturn(java.util.Optional.of(existingProdotto));

        prodottoService.update(prodottoReq);

        verify(prodottoRepository, times(1)).save(existingProdotto);
    }

    @Test
    void removeProdotto_ShouldDeleteProduct() throws Exception {
        Prodotto prodotto = new Prodotto();
        prodotto.setId(1);

        when(prodottoRepository.findById(prodotto.getId())).thenReturn(java.util.Optional.of(prodotto));

        prodottoService.removeProdotto(prodottoReq);

        verify(prodottoRepository, times(1)).delete(prodotto);
    }
}

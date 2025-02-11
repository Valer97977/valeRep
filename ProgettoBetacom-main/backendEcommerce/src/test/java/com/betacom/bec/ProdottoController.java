package com.betacom.bec.controller;

import com.betacom.bec.dto.ProdottoDTO;
import com.betacom.bec.request.ProdottoReq;
import com.betacom.bec.response.ResponseBase;
import com.betacom.bec.response.ResponseList;
import com.betacom.bec.services.interfaces.ProdottoServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProdottoControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ProdottoServices prodottoServices;

    @InjectMocks
    private ProdottoController prodottoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(prodottoController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createProdotto_ShouldReturnSuccess() throws Exception {
        ProdottoReq prodottoReq = new ProdottoReq();
        prodottoReq.setNome("Prodotto 1");
        prodottoReq.setMarca("Marca A");
        prodottoReq.setCategoria("Categoria 1");
        prodottoReq.setDescrizione("Descrizione prodotto");
        prodottoReq.setPrezzo(100.0);
        prodottoReq.setquantitaDisponibile(10);
        prodottoReq.setUrlImg("url.com");
        prodottoReq.setSize("M");
        prodottoReq.setColore("Rosso");

        ResponseBase responseBase = new ResponseBase();
        responseBase.setRc(true);

        when(prodottoServices.create(prodottoReq)).thenReturn(responseBase);

        mockMvc.perform(post("/rest/prodotto/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prodottoReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true));
    }

    @Test
    void updateProdotto_ShouldReturnSuccess() throws Exception {
        ProdottoReq prodottoReq = new ProdottoReq();
        prodottoReq.setId(1);
        prodottoReq.setPrezzo(150.0);
        prodottoReq.setquantitaDisponibile(20);

        ResponseBase responseBase = new ResponseBase();
        responseBase.setRc(true);

        when(prodottoServices.update(prodottoReq)).thenReturn(responseBase);

        mockMvc.perform(post("/rest/prodotto/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prodottoReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true));
    }

    @Test
    void listByCategoria_ShouldReturnListOfProdotti() throws Exception {
        ProdottoDTO prodottoDTO = new ProdottoDTO();
        prodottoDTO.setNome("Prodotto 1");

        ResponseList<ProdottoDTO> responseList = new ResponseList<>();
        responseList.setDati(List.of(prodottoDTO));
        responseList.setRc(true);

        when(prodottoServices.listByCategoria("Categoria 1")).thenReturn(responseList);

        mockMvc.perform(get("/rest/prodotto/listByCategoria/Categoria 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.dati[0].nome").value("Prodotto 1"));
    }

    @Test
    void removeProdotto_ShouldReturnSuccess() throws Exception {
        ProdottoReq prodottoReq = new ProdottoReq();
        prodottoReq.setId(1);

        ResponseBase responseBase = new ResponseBase();
        responseBase.setRc(true);

        doNothing().when(prodottoServices).removeProdotto(prodottoReq);

        mockMvc.perform(post("/rest/prodotto/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prodottoReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true));
    }

    // Test per errori
    @Test
    void createProdotto_ShouldReturnError_WhenMissingFields() throws Exception {
        ProdottoReq prodottoReq = new ProdottoReq(); // Missing required fields
        ResponseBase responseBase = new ResponseBase();
        responseBase.setRc(false);

        when(prodottoServices.create(prodottoReq)).thenThrow(new Exception("Prodotto già presente"));

        mockMvc.perform(post("/rest/prodotto/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prodottoReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.rc").value(false))
                .andExpect(jsonPath("$.msg").value("Prodotto già presente"));
    }
}

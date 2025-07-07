package com.group.groupemaker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.groupemaker.model.Historique;
import com.group.groupemaker.repository.HistoriqueRepository;
import com.group.groupemaker.service.JwtAuthenticationFilter;
import com.group.groupemaker.service.JwtService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HistoriqueController.class)
@AutoConfigureMockMvc(addFilters = false) // désactive les filtres de sécurité (JWT, etc.)
public class HistoriqueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoriqueRepository historiqueRepository;

    @MockBean
    private JwtAuthenticationFilter jwtFilter;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllHistoriques() throws Exception {
        Historique historique = new Historique();
        historique.setId(1L);
        historique.setNomListe("Promo JS");

        when(historiqueRepository.findAll()).thenReturn(List.of(historique));

        mockMvc.perform(get("/historiques"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomListe").value("Promo JS"));
    }

    @Test
    void shouldCreateHistorique() throws Exception {
        Historique historique = new Historique();
        historique.setNomListe("Promo Java");

        when(historiqueRepository.save(any(Historique.class))).thenReturn(historique);

        mockMvc.perform(post("/historiques")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(historique)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomListe").value("Promo Java"));
    }
}

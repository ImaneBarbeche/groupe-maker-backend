package com.group.groupemaker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.groupemaker.model.Formateur;
import com.group.groupemaker.model.Utilisateur;
import com.group.groupemaker.repository.FormateurRepository;
import com.group.groupemaker.service.JwtAuthenticationFilter;
import com.group.groupemaker.service.JwtUtil;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FormateurController.class)
@AutoConfigureMockMvc(addFilters = false) // désactive les filtres de sécurité (JWT, etc.)
public class FormateurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FormateurRepository formateurRepository;

    @MockBean
    private JwtAuthenticationFilter jwtFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllFormateurs() throws Exception {
        Formateur formateur = new Formateur();
        formateur.setId(1L);
        formateur.setExpertise("IA");

        when(formateurRepository.findAll()).thenReturn(List.of(formateur));

        mockMvc.perform(get("/formateurs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].expertise").value("IA"));
    }

    @Test
    void shouldGetFormateurById() throws Exception {
        Formateur formateur = new Formateur();
        formateur.setId(1L);
        formateur.setExpertise("IA");

        when(formateurRepository.findById(1L)).thenReturn(Optional.of(formateur));

        mockMvc.perform(get("/formateurs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expertise").value("IA"));
    }

    @Test
    void shouldReturn404IfFormateurNotFound() throws Exception {
        when(formateurRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/formateurs/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateFormateur() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        Formateur formateur = new Formateur();
        formateur.setExpertise("Angular");
        formateur.setUtilisateur(utilisateur);

        when(formateurRepository.save(any(Formateur.class))).thenReturn(formateur);

        mockMvc.perform(post("/formateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(formateur)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expertise").value("Angular"));
    }

    @Test
    void shouldUpdateFormateur() throws Exception {
        Formateur ancien = new Formateur();
        ancien.setId(1L);
        ancien.setExpertise("Java");

        Formateur modifie = new Formateur();
        modifie.setExpertise("Spring");

        when(formateurRepository.findById(1L)).thenReturn(Optional.of(ancien));
        // simulate update
        ancien.setExpertise(modifie.getExpertise());
        when(formateurRepository.save(any(Formateur.class))).thenReturn(ancien);

        mockMvc.perform(put("/formateurs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expertise").value("Spring"));
    }

    @Test
    void shouldDeleteFormateur() throws Exception {
        Formateur formateur = new Formateur();
        formateur.setId(1L);
        formateur.setExpertise("DevOps");

        when(formateurRepository.findById(1L)).thenReturn(Optional.of(formateur));

        mockMvc.perform(delete("/formateurs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expertise").value("DevOps"));
    }
}
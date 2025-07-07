package com.group.groupemaker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.groupemaker.model.Liste;
import com.group.groupemaker.repository.ListeRepository;
import com.group.groupemaker.repository.UtilisateurRepository;
import com.group.groupemaker.service.JwtAuthenticationFilter;
import com.group.groupemaker.service.JwtService;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ListeController.class)
@AutoConfigureMockMvc(addFilters = false) // << désactive les filtres de sécurité pour le test
public class ListeControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ListeRepository listeRepository;

        @MockBean
        private UtilisateurRepository utilisateurRepository;

        @MockBean
        private JwtAuthenticationFilter jwtFilter;

        @MockBean
        private JwtService jwtService;

        @Autowired
        private ObjectMapper objectMapper;

        // Vérifie que GET /listes/{id} retourne la liste attendue avec code
        // 200.
        @Test
        void shouldReturnListeById() throws Exception {
                // une liste existante simulée
                Liste liste = new Liste();
                liste.setId(1L);
                liste.setNom("CDA");
                when(listeRepository.findById(1L)).thenReturn(Optional.of(liste));
                // requête GET sur /listes/1, on vérifie la réponse
                mockMvc.perform(get("/listes/1"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.nom").value("CDA"));
        }

        // Vérifie que GET /listes/{id} renvoie 404 si la liste n'existe pas.
        @Test
        void shouldReturn404WhenListeNotFound() throws Exception {
                // le repository renvoie un Optional vide
                when(listeRepository.findById(99L)).thenReturn(Optional.empty());

                // GET /listes/99 → 404 attendu
                mockMvc.perform(get("/listes/99"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void shouldUpdateListeIfExists() throws Exception {
                // Arrange
                Liste ancienne = new Liste();
                ancienne.setId(1L);
                ancienne.setNom("Ancien nom");
                Liste modifiee = new Liste();
                modifiee.setNom("Nouveau nom");

                when(listeRepository.findById(1L)).thenReturn(Optional.of(ancienne));
                when(listeRepository.save(any(Liste.class))).thenAnswer(invocation -> invocation.getArgument(0));

                // Act & Assert
                mockMvc.perform(
                                put("/listes/1")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(modifiee)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.nom").value("Nouveau nom"));
        }

        // Vérifie que PUT /listes/{id} renvoie 404 si la liste n'existe pas.
        @Test
        void shouldReturn404WhenListeNotUpdated() throws Exception {
                // le repository renvoie un Optional vide
                when(listeRepository.findById(42L)).thenReturn(Optional.empty());

                // PUT /listes/99 → 404 attendu
                mockMvc.perform(
                                put("/listes/42")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content("{\"nom\": \"Nouveau nom\", \"utilisateur\": null}"))
                                .andExpect(status().isNotFound());

        }

        @Test
        void shouldDeleteListeById() throws Exception {
                // on simule une liste
                Liste liste = new Liste();
                liste.setId(2L);
                liste.setNom("CDA");

                when(listeRepository.findById(2L)).thenReturn(Optional.of(liste));

                mockMvc.perform(delete("/listes/2"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.nom").value("CDA"));
        }

        // Vérifie que DELETE /listes/{id} renvoie 404 si la liste n'existe pas.
        @Test
        void shouldReturn404WhenDeletingNonExistingListe() throws Exception {
                // Simule un Optional vide → la liste n'existe pas
                when(listeRepository.findById(999L)).thenReturn(Optional.empty());

                mockMvc.perform(delete("/listes/999"))
                                .andExpect(status().isNotFound());
        }

}

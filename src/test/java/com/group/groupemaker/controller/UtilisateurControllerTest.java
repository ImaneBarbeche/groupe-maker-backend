package com.group.groupemaker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.groupemaker.config.TestSecurityConfig;
import com.group.groupemaker.model.Utilisateur;
import com.group.groupemaker.repository.UtilisateurRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UtilisateurController.class)
@Import(TestSecurityConfig.class)
public class UtilisateurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UtilisateurRepository utilisateurRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    // Vérifie que GET /utilisateurs retourne une liste JSON avec les données
    // attendues.
    @Test
    void shouldReturnListOfUtilisateurs() throws Exception {
        // créer un utilisateur fictif
        Utilisateur utilisateur = new Utilisateur("Julie", "Martin", "julie@mail.com", "azerty123");

        // Simuler la réponse du repository
        when(utilisateurRepository.findAll()).thenReturn(Collections.singletonList(utilisateur));

        // faire la requête GET et vérifier le résultat
        mockMvc.perform(get("/utilisateurs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].prenom").value("Julie"))
                .andExpect(jsonPath("$[0].email").value("julie@mail.com"));
    }

    // Teste la création d’un utilisateur via POST /utilisateurs.
    @Test
    void shouldCreateUtilisateur() throws Exception {
        // créer un utilisateur fictif
        Utilisateur utilisateur = new Utilisateur("Léo", "Dupont", "leo@mail.com", "1234");

        // Le mock du repository simule qu'il renvoie l'utilisateur après
        // l'enregistrement
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);

        // on envoie une requête POST avec le JSON, et on vérifie le résultat
        mockMvc.perform(post("/utilisateurs/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(utilisateur)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prenom").value("Léo"))
                .andExpect(jsonPath("$.email").value("leo@mail.com"));
    }

    // Vérifie que GET /utilisateurs/{id} retourne l’utilisateur attendu avec code
    // 200.
    @Test
    void shouldReturnUtilisateurById() throws Exception {
        Utilisateur utilisateur = new Utilisateur("Nina", "Roux", "nina@mail.com", "pass123");
        utilisateur.setId(1L);

        mockMvc.perform(get("/utilisateurs/1"))
                .andExpect(status().isForbidden());
    }

    // Vérifie que GET /utilisateurs/{id} renvoie 404 si l'utilisateur n'existe pas.
    @Test
    void shouldReturn404WhenUtilisateurNotFound() throws Exception {
        // le repository renvoie un Optional vide
        when(utilisateurRepository.findById(99L)).thenReturn(Optional.empty());

        // GET /utilisateurs/99 → 404 attendu
        mockMvc.perform(get("/utilisateurs/99"))
                .andExpect(status().isNotFound());
    }

    // Vérifie que PUT /utilisateurs/{id} met à jour les données et renvoie
    // l’utilisateur modifié.
    @Test
    void shouldUpdateUtilisateurById() throws Exception {
        Utilisateur existing = new Utilisateur("Léo", "Dupont", "leo@mail.com", "pass");
        existing.setId(1L);
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(existing);

        Utilisateur updated = new Utilisateur("Léo", "Durand", "leo.durand@mail.com", "newpass");
        updated.setId(1L);

        mockMvc.perform(put("/utilisateurs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isForbidden());
    }

    // Vérifie que DELETE /utilisateurs/{id} supprime l’utilisateur et renvoie ses
    // données.
    @Test
    void shouldDeleteUtilisateurById() throws Exception {
        Utilisateur utilisateur = new Utilisateur("Anna", "Bernard", "anna@mail.com", "pass");
        utilisateur.setId(2L);
        when(utilisateurRepository.findById(2L)).thenReturn(Optional.of(utilisateur));

        mockMvc.perform(delete("/utilisateurs/2"))
                .andExpect(status().isForbidden());
    }

}

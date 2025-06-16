package com.group.groupemaker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.groupemaker.config.TestSecurityConfig;
import com.group.groupemaker.model.Liste;
import com.group.groupemaker.model.Personne;
import com.group.groupemaker.repository.PersonneRepository;

@WebMvcTest(PersonneController.class)
@Import(TestSecurityConfig.class)
public class PersonneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonneRepository personneRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreatePersonne() throws Exception {
        // Arrange – crée une liste fictive
        Liste liste = new Liste();
        liste.setId(1L);
        liste.setNom("CDA");

        // crée une personne liée à cette liste
        Personne personne = new Personne("Duponte", "Féminin", 3, true, 3, "Timide", 23, liste);

        when(personneRepository.save(any(Personne.class))).thenReturn(personne);

        // Act & Assert – envoie une requête POST avec la personne
        mockMvc.perform(post("/personnes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personne)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Duponte"))
                .andExpect(jsonPath("$.genre").value("Féminin"))
                .andExpect(jsonPath("$.aisanceFr").value(3))
                .andExpect(jsonPath("$.ancienDwwm").value(true))
                .andExpect(jsonPath("$.niveauTechnique").value(3))
                .andExpect(jsonPath("$.profil").value("Timide"))
                .andExpect(jsonPath("$.age").value(23))
                .andExpect(jsonPath("$.liste.nom").value("CDA"));
    }

    @Test
    void testDeletePersonneById() throws Exception {
        // Arrange – crée une liste fictive
        Liste liste = new Liste();
        liste.setId(1L);
        liste.setNom("CDA");
        // Crée une personne fictive liée à la liste
        Personne personne = new Personne("Duponte", "Féminin", 3, true, 3, "Timide", 23, liste);
        // Simule une réponse du repository : findById(2L) retourne une personne
        // fictive.
        when(personneRepository.findById(2L)).thenReturn(Optional.of(personne));
        // Act & Assert – envoie une requête DELETE
        mockMvc.perform(delete("/personnes/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Duponte"))
                .andExpect(jsonPath("$.liste.nom").value("CDA"));
    }

    @Test
    void testGetAllPersonnes() throws Exception {
        Liste liste = new Liste();
        liste.setId(1L);
        liste.setNom("CDA");

        Personne p1 = new Personne("Alice", "Féminin", 3, true, 2, "Timide", 25, liste);
        Personne p2 = new Personne("Bob", "Masculin", 2, false, 3, "À l’aise", 28, liste);

        when(personneRepository.findAll()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/personnes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nom").value("Alice"))
                .andExpect(jsonPath("$[1].nom").value("Bob"));
    }

    @Test
    void testGetPersonneById() throws Exception {
        // Arrange
        Liste liste = new Liste();
        liste.setId(1L);
        liste.setNom("CDA");

        Personne personne = new Personne("Charlie", "Masculin", 4, false, 4, "À l’aise", 30, liste);

        when(personneRepository.findById(3L)).thenReturn(Optional.of(personne));

        // Act & Assert
        mockMvc.perform(get("/personnes/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Charlie"))
                .andExpect(jsonPath("$.genre").value("Masculin"))
                .andExpect(jsonPath("$.liste.nom").value("CDA"));
    }

    @Test
    void testUpdatePersonneById() throws Exception {
        // Arrange
        Liste liste = new Liste();
        liste.setId(1L);
        liste.setNom("CDA");

        // ancienne version (en base)
        Personne ancienne = new Personne("Charlie", "Masculin", 4, false, 4, "À l’aise", 30, liste);
        // version modifiée (envoyée dans la requête)
        Personne modifiee = new Personne("Charlène", "Féminin", 2, true, 2, "Timide", 28, liste);

        when(personneRepository.findById(3L)).thenReturn(Optional.of(ancienne));
        when(personneRepository.save(any(Personne.class))).thenReturn(modifiee);

        // Act & Assert
        mockMvc.perform(put("/personnes/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifiee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Charlène"))
                .andExpect(jsonPath("$.genre").value("Féminin"))
                .andExpect(jsonPath("$.aisanceFr").value(2))
                .andExpect(jsonPath("$.ancienDwwm").value(true))
                .andExpect(jsonPath("$.niveauTechnique").value(2))
                .andExpect(jsonPath("$.profil").value("Timide"))
                .andExpect(jsonPath("$.age").value(28))
                .andExpect(jsonPath("$.liste.nom").value("CDA"));
    }

}

package com.group.groupemaker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.groupemaker.config.TestSecurityConfig;
import com.group.groupemaker.dto.PersonneDTO;
import com.group.groupemaker.model.Genre;
import com.group.groupemaker.model.Profil;
import com.group.groupemaker.service.PersonneService;

@WebMvcTest(PersonneController.class)
@Import(TestSecurityConfig.class)
public class PersonneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonneService personneService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetPersonnesParListe() throws Exception {
        // Arrange
        Long listeId = 1L;
        PersonneDTO p1 = new PersonneDTO(1L, "Alice", Genre.FEMININ, 3, true, 2, Profil.TIMIDE, 25, listeId);
        PersonneDTO p2 = new PersonneDTO(2L, "Bob", Genre.MASCULIN, 2, false, 3, Profil.A_LAISE, 28, listeId);

        when(personneService.getPersonnesParListe(listeId)).thenReturn(List.of(p1, p2));

        // Act & Assert
        mockMvc.perform(get("/personnes/liste/" + listeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nom").value("Alice"))
                .andExpect(jsonPath("$[0].genre").value("FEMININ"))
                .andExpect(jsonPath("$[0].aisanceFr").value(3))
                .andExpect(jsonPath("$[0].ancienDwwm").value(true))
                .andExpect(jsonPath("$[0].profil").value("TIMIDE"))
                .andExpect(jsonPath("$[1].nom").value("Bob"))
                .andExpect(jsonPath("$[1].genre").value("MASCULIN"))
                .andExpect(jsonPath("$[1].profil").value("A_LAISE"));
    }

    @Test
    void testAjouterPersonne() throws Exception {
        // Arrange
        Long listeId = 1L;
        PersonneDTO personneDTO = new PersonneDTO(null, "Charlie", Genre.MASCULIN, 4, false, 4, Profil.A_LAISE, 30, listeId);
        PersonneDTO personneSauvegardee = new PersonneDTO(3L, "Charlie", Genre.MASCULIN, 4, false, 4, Profil.A_LAISE, 30, listeId);

        when(personneService.ajouterPersonne(any(PersonneDTO.class), eq(listeId))).thenReturn(personneSauvegardee);

        // Act & Assert
        mockMvc.perform(post("/personnes/liste/" + listeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personneDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nom").value("Charlie"))
                .andExpect(jsonPath("$.genre").value("MASCULIN"))
                .andExpect(jsonPath("$.aisanceFr").value(4))
                .andExpect(jsonPath("$.ancienDwwm").value(false))
                .andExpect(jsonPath("$.niveauTechnique").value(4))
                .andExpect(jsonPath("$.profil").value("A_LAISE"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.utilisateurId").value(listeId));
    }

    @Test
    void testSupprimerPersonne() throws Exception {
        // Arrange
        Long personneId = 1L;

        // Act & Assert
        mockMvc.perform(delete("/personnes/" + personneId))
                .andExpect(status().isOk());

        // Vérifier que le service a bien été appelé
        verify(personneService).supprimerPersonne(personneId);
    }

    @Test
    void testAjouterPersonneAvecDonneesInvalides() throws Exception {
        // Arrange
        Long listeId = 1L;
        // Personne avec un nom trop court (moins de 3 caractères)
        PersonneDTO personneInvalide = new PersonneDTO(null, "Al", Genre.FEMININ, 3, true, 2, Profil.TIMIDE, 25, listeId);

        // Act & Assert
        mockMvc.perform(post("/personnes/liste/" + listeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personneInvalide)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetPersonnesListeVide() throws Exception {
        // Arrange
        Long listeId = 999L;
        when(personneService.getPersonnesParListe(listeId)).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/personnes/liste/" + listeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testAjouterPersonneAvecTousLesChamps() throws Exception {
        // Arrange
        Long listeId = 1L;
        PersonneDTO personneComplete = new PersonneDTO(
            null, 
            "Marie-Claire", 
            Genre.FEMININ, 
            2, // Aisance français
            true, // Ancien DWWM
            3, // Niveau technique
            Profil.RESERVE, 
            32, // Âge
            listeId
        );
        
        PersonneDTO personneSauvegardee = new PersonneDTO(
            4L, 
            "Marie-Claire", 
            Genre.FEMININ, 
            2, 
            true, 
            3, 
            Profil.RESERVE, 
            32, 
            listeId
        );

        when(personneService.ajouterPersonne(any(PersonneDTO.class), eq(listeId))).thenReturn(personneSauvegardee);

        // Act & Assert
        mockMvc.perform(post("/personnes/liste/" + listeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personneComplete)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.nom").value("Marie-Claire"))
                .andExpect(jsonPath("$.genre").value("FEMININ"))
                .andExpect(jsonPath("$.aisanceFr").value(2))
                .andExpect(jsonPath("$.ancienDwwm").value(true))
                .andExpect(jsonPath("$.niveauTechnique").value(3))
                .andExpect(jsonPath("$.profil").value("RESERVE"))
                .andExpect(jsonPath("$.age").value(32))
                .andExpect(jsonPath("$.utilisateurId").value(listeId));
    }
}

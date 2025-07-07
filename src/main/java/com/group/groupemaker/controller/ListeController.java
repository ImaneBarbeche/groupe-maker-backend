package com.group.groupemaker.controller;

import com.group.groupemaker.dto.ListeDTO;
import com.group.groupemaker.dto.PersonneDTO;
import com.group.groupemaker.model.Utilisateur;
import com.group.groupemaker.repository.UtilisateurRepository;
import com.group.groupemaker.service.JwtService;
import com.group.groupemaker.service.ListeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/listes")
public class ListeController {

    private final ListeService listeService;
    private final UtilisateurRepository utilisateurRepository;
    private final JwtService jwtService;

    public ListeController(ListeService listeService, UtilisateurRepository utilisateurRepository, JwtService jwtService) {
        this.listeService = listeService;
        this.utilisateurRepository = utilisateurRepository;
        this. jwtService = jwtService;
    }

    @GetMapping("/mine")
public ResponseEntity<?> getMesListes(@CookieValue(name = "jwt", required = false) String token) {
    if (token == null || !jwtService.isTokenValid(token)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
            "success", false,
            "message", "Token manquant ou invalide"
        ));
    }

    String email = jwtService.extractEmail(token);
    Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

    List<ListeDTO> listes = listeService.getListesByUtilisateur(utilisateur.getId());
    return ResponseEntity.ok(Map.of(
        "success", true,
        "data", listes
    ));
}

   @PostMapping
public ResponseEntity<?> creerListe(@RequestBody ListeDTO dto, @CookieValue(name = "jwt", required = false) String token) {
    if (token == null || !jwtService.isTokenValid(token)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
            "success", false,
            "message", "Token manquant ou invalide"
        ));
    }

    String email = jwtService.extractEmail(token);
    Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

    ListeDTO savedListe = listeService.createListe(dto, utilisateur.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
        "success", true,
        "data", savedListe
    ));
}

@PostMapping("/{listeId}/personnes")
public ResponseEntity<?> ajouterPersonnes(@PathVariable Long listeId, @RequestBody List<PersonneDTO> personnes) {
    try {
        listeService.ajouterPersonnesAListe(listeId, personnes);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Personnes ajoutées avec succès"
        ));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
            "success", false,
            "message", "Erreur lors de l'ajout des personnes : " + e.getMessage()
        ));
    }
}

@PutMapping("/{listeId}")
public ResponseEntity<?> modifierListe(@PathVariable Long listeId, @RequestBody ListeDTO dto) {
    try {
        ListeDTO updatedListe = listeService.modifierListe(listeId, dto);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", updatedListe
        ));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
            "success", false,
            "message", "Erreur lors de la modification de la liste : " + e.getMessage()
        ));
    }
}

@PutMapping("/{listeId}/personnes/{personneId}")
public ResponseEntity<?> modifierPersonne(@PathVariable Long listeId, @PathVariable Long personneId, @RequestBody PersonneDTO dto) {
    try {
        PersonneDTO updatedPersonne = listeService.modifierPersonne(listeId, personneId, dto);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", updatedPersonne
        ));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
            "success", false,
            "message", "Erreur lors de la modification de la personne : " + e.getMessage()
        ));
    }
}
    @DeleteMapping("/{listeId}")
public ResponseEntity<?> supprimerListe(@PathVariable Long listeId) {
    try {
        listeService.supprimerListe(listeId);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Liste supprimée avec succès"
        ));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
            "success", false,
            "message", "Erreur lors de la suppression de la liste : " + e.getMessage()
        ));
    }
}

@DeleteMapping("/{listeId}/personnes/{personneId}")
public ResponseEntity<?> supprimerPersonne(@PathVariable Long listeId, @PathVariable Long personneId) {
    try {
        listeService.supprimerPersonne(listeId, personneId);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Personne supprimée avec succès"
        ));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
            "success", false,
            "message", "Erreur lors de la suppression de la personne : " + e.getMessage()
        ));
    }
}

    public Utilisateur getUtilisateurConnecte(Authentication authentication) {
    String email = (String) authentication.getPrincipal();
    return utilisateurRepository.findByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
}

}

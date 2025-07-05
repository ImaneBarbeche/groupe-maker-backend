package com.group.groupemaker.controller;

import com.group.groupemaker.dto.ListeDTO;
import com.group.groupemaker.model.Utilisateur;
import com.group.groupemaker.repository.UtilisateurRepository;
import com.group.groupemaker.service.ListeService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/listes")
public class ListeController {

    private final ListeService listeService;
    private final UtilisateurRepository utilisateurRepository;

    public ListeController(ListeService listeService, UtilisateurRepository utilisateurRepository) {
        this.listeService = listeService;
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping("/mine")
    public List<ListeDTO> getMesListes(Authentication authentication) {
        Utilisateur utilisateur = getUtilisateurConnecte(authentication);
        return listeService.getListesByUtilisateur(utilisateur.getId());
    }

    @PostMapping
    public ListeDTO creerListe(@RequestBody ListeDTO dto, Authentication authentication) {
        Utilisateur utilisateur = getUtilisateurConnecte(authentication);
        return listeService.createListe(dto, utilisateur.getId());
    }

    // Optionnel : suppression directe depuis le service ou via repo
    @DeleteMapping("/{id}")
    public void supprimerListe(@PathVariable Long id) {
        listeService.supprimerListe(id); // À créer si tu veux
    }

    public Utilisateur getUtilisateurConnecte(Authentication authentication) {
    String email = (String) authentication.getPrincipal();
    return utilisateurRepository.findByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
}

}

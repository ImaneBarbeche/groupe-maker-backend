package com.group.groupemaker.controller;

import com.group.groupemaker.dto.GroupeDTO;
import com.group.groupemaker.model.Utilisateur;
import com.group.groupemaker.repository.UtilisateurRepository;
import com.group.groupemaker.service.GroupeService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/groupes")
public class GroupeController {

    private final GroupeService groupeService;
    private final UtilisateurRepository utilisateurRepository;

    public GroupeController(GroupeService groupeService, UtilisateurRepository utilisateurRepository) {
        this.groupeService = groupeService;
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping("/liste/{listeId}")
    public List<GroupeDTO> getGroupesParListe(@PathVariable Long listeId) {
        return groupeService.getGroupesParListe(listeId);
    }

    @PostMapping("/liste/{listeId}")
    public GroupeDTO creerGroupe(@RequestBody GroupeDTO dto, @PathVariable Long listeId, Authentication auth) {
        // Optionnel : tu peux vérifier que l’utilisateur connecté possède bien la liste
        getUtilisateurConnecte(auth); // juste pour forcer la vérification
        return groupeService.creerGroupe(dto, listeId);
    }

    @DeleteMapping("/{id}")
    public void supprimerGroupe(@PathVariable Long id) {
        groupeService.supprimerGroupe(id);
    }

    private Utilisateur getUtilisateurConnecte(Authentication auth) {
        return utilisateurRepository.findByEmail(auth.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));
    }
}

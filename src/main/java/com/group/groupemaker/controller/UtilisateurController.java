package com.group.groupemaker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.group.groupemaker.model.Utilisateur;
import com.group.groupemaker.repository.UtilisateurRepository;
import org.springframework.web.bind.annotation.PutMapping;

@RestController // Gérer les requêtes REST, renvoyer du JSON
@RequestMapping("/utilisateurs") // Toutes les routes commenceront par /utilisateurs
public class UtilisateurController {

    @Autowired // On injecte UtilisateurRepository
    private UtilisateurRepository utilisateurRepository;

    @GetMapping // On répond à une requête GET avec la liste des utilisateurs
    public List<Utilisateur> getAll() {
        return utilisateurRepository.findAll();
    }

    @PostMapping
    public Utilisateur createUtilisateur(@RequestBody Utilisateur utilisateur) { // @RequestBody pour recevoir les
                                                                                 // données JSON envoyées par le client
        return utilisateurRepository.save(utilisateur);
    }

    @GetMapping("/utilisateurs/{id}")
    public Utilisateur getUtilisateurById(@PathVariable Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));
    }

    /**
     * 
     * Met à jour un utilisateur existant en base à partir de son id.
     * Si l'utilisateur est trouvé, on met à jour ses champs avec les nouvelles
     * données reçues.
     * Sinon, on retourne une erreur
     * 
     */
    @PutMapping("/utilisateurs/{id}")
    public Utilisateur putUtilisateurById(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        Utilisateur existing = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));

        existing.setPrenom(utilisateur.getPrenom());
        existing.setNom(utilisateur.getNom());
        existing.setEmail(utilisateur.getEmail());
        existing.setMotDePasse(utilisateur.getMotDePasse());

        return utilisateurRepository.save(existing);

    }

    /**
     * Supprime un utilisateur à partir de son identifiant.
     * Si l'utilisateur est trouvé, il est supprimé de la base et retourné en
     * réponse.
     * Sinon, la méthode retourne une erreur
     */
    @DeleteMapping("/utilisateurs/{id}")
    public Utilisateur deleteUtilisateurById(@PathVariable Long id) {
        Utilisateur existing = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));
            utilisateurRepository.deleteById(id);
            return existing;
    }

}
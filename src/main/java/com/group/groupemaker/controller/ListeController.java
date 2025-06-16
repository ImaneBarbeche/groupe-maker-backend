package com.group.groupemaker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.group.groupemaker.model.Liste;
import com.group.groupemaker.repository.ListeRepository;
import org.springframework.web.bind.annotation.PutMapping;

@RestController // Gérer les requêtes REST, renvoyer du JSON
@RequestMapping("/listes") // Toutes les routes commenceront par /listes
public class ListeController {

    private final ListeRepository listeRepository;

    public ListeController(ListeRepository listeRepository) {
        this.listeRepository = listeRepository;
    }

    @GetMapping // On répond à une requête GET avec la liste des utilisateurs
    public List<Liste> getAllListes() {
        return listeRepository.findAll();
    }

    // Crée une nouvelle liste à partir des données reçues en JSON.
    // Enregistre l’objet en base et renvoie la liste créée.
    @PostMapping
    public Liste addListe(@RequestBody Liste liste) {

        return listeRepository.save(liste);
    }

    // Supprime une liste à partir de son identifiant.
    // Si la liste est trouvée, elle est supprimée et renvoyée en réponse.
    // Sinon, une erreur 404 est retournée.

    @DeleteMapping("/{id}")
    public Liste deleteListeById(@PathVariable Long id) {
        Liste existing = listeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Liste not found"));

        listeRepository.deleteById(id);
        return existing;
    }

    // Récupère une liste à partir de son identifiant.
    // Si elle est trouvée, la retourne en JSON.
    // Sinon, déclenche une erreur 404.
    @GetMapping("/{id}")
    public Liste getListeById(@PathVariable Long id) {
        Liste existing = listeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Liste not found"));
        return existing;
    }

    // Met à jour le nom d'une liste existante à partir de son identifiant.
    @PutMapping("/{id}")
    public Liste putListeById(@PathVariable Long id, @RequestBody Liste liste) {
        Liste existing = listeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Liste not found"));
        existing.setNom(liste.getNom());
        return listeRepository.save(existing);
    }
}

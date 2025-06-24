package com.group.groupemaker.controller;

import com.group.groupemaker.model.Personne;
import com.group.groupemaker.repository.PersonneRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/personnes")
public class PersonneController {

    private final PersonneRepository personneRepository;

    public PersonneController(PersonneRepository personneRepository) {
        this.personneRepository = personneRepository;
    }

    // on répond à une requête GET avec la liste des personnes
    @GetMapping
    public List<Personne> getAllPersonnes() {
        return personneRepository.findAll();
    }

    // récupérer les personnes par id
    @GetMapping("/{id}")
    public Personne getPersonneById(@PathVariable Long id) {
        return personneRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personne non trouvée"));
    }

    // création de personnes
     @PostMapping
    public Personne createPersonne(@RequestBody Personne personne) { // @RequestBody pour recevoir les
                                                                                 // données JSON envoyées par le client
        return personneRepository.save(personne);
    }

    /**
     * 
     * Met à jour une personne existante en base à partir de son id.
     * Si la personne est trouvée, on met à jour ses champs avec les nouvelles
     * données reçues.
     * Sinon, on retourne une erreur
     * 
     */
    @PutMapping("/{id}")
    public Personne putPersonneById(@PathVariable Long id, @RequestBody Personne personne) {
        Personne existing = personneRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personne non trouvée"));

        existing.setNom(personne.getNom());
        existing.setGenre(personne.getGenre());
        existing.setAisanceFr(personne.getAisanceFr());
        existing.setAncienDwwm(personne.getAncienDwwm());
        existing.setNiveauTechnique(personne.getNiveauTechnique());
        existing.setProfil(personne.getProfil());
        existing.setAge(personne.getAge());


        return personneRepository.save(existing);

    }

    /**
     * Supprime une personne à partir de son identifiant.
     * Si la personne est trouvée, elle est supprimée de la base et retournée en
     * réponse.
     * Sinon, la méthode retourne une erreur
     */
    @DeleteMapping("/{id}")
    public Personne deletePersonneById(@PathVariable Long id) {
        Personne existing = personneRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personne non trouvée"));
            personneRepository.deleteById(id);
            return existing;
    }
}

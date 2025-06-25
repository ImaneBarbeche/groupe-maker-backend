package com.group.groupemaker.controller;

import com.group.groupemaker.dto.PersonneDTO;
import com.group.groupemaker.service.PersonneService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personnes")
public class PersonneController {

    private final PersonneService personneService;

    public PersonneController(PersonneService personneService) {
        this.personneService = personneService;
    }

    @GetMapping("/liste/{listeId}")
    public List<PersonneDTO> getPersonnesParListe(@PathVariable Long listeId) {
        return personneService.getPersonnesParListe(listeId);
    }

    @PostMapping("/liste/{listeId}")
    public PersonneDTO ajouterPersonne(@RequestBody PersonneDTO dto, @PathVariable Long listeId) {
        return personneService.ajouterPersonne(dto, listeId);
    }

    @DeleteMapping("/{id}")
    public void supprimerPersonne(@PathVariable Long id) {
        personneService.supprimerPersonne(id);
    }
}

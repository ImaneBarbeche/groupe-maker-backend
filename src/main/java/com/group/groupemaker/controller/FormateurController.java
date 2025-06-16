package com.group.groupemaker.controller;

import com.group.groupemaker.model.Formateur;
import com.group.groupemaker.repository.FormateurRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/formateurs")
public class FormateurController {

    private final FormateurRepository formateurRepository;

    public FormateurController(FormateurRepository formateurRepository) {
        this.formateurRepository = formateurRepository;
    }

    @GetMapping
    public List<Formateur> getAll() {
        return formateurRepository.findAll();
    }

    @GetMapping("/{id}")
    public Formateur getById(@PathVariable Long id) {
        return formateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Formateur non trouvé"));
    }

    @PostMapping
    public Formateur create(@RequestBody Formateur formateur) {
        return formateurRepository.save(formateur);
    }

    @PutMapping("/{id}")
    public Formateur update(@PathVariable Long id, @RequestBody Formateur formateur) {
        Formateur existing = formateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Formateur non trouvé"));

        existing.setExpertise(formateur.getExpertise());
        existing.setUtilisateur(formateur.getUtilisateur());

        return formateurRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public Formateur delete(@PathVariable Long id) {
        Formateur existing = formateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Formateur non trouvé"));
        formateurRepository.deleteById(id);
        return existing;
    }
}

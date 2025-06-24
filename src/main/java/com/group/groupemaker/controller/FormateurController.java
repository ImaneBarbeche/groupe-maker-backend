package com.group.groupemaker.controller;

import com.group.groupemaker.dto.FormateurDTO;
import com.group.groupemaker.model.Formateur;
import com.group.groupemaker.model.Utilisateur;
import com.group.groupemaker.repository.FormateurRepository;
import com.group.groupemaker.repository.UtilisateurRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/formateurs")
public class FormateurController {

    private final FormateurRepository formateurRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public FormateurController(FormateurRepository formateurRepository) {
        this.formateurRepository = formateurRepository;
    }

    @GetMapping
    public List<Formateur> getAllFormateurs() {
        return formateurRepository.findAll();
    }
    
    @PostMapping("/register")
    public Formateur registerFormateur(@RequestBody FormateurDTO dto) {
        System.out.println(">>> Début création formateur");

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setRole("FORMATEUR");
        utilisateur.setActive(true);
        utilisateur.setDateCreation(LocalDateTime.now());

        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
        System.out.println(">>> Utilisateur enregistré avec id : " + savedUtilisateur.getId());

        Formateur formateur = new Formateur();
        formateur.setExpertise(dto.getExpertise());
        formateur.setUtilisateur(savedUtilisateur);

        Formateur savedFormateur = formateurRepository.save(formateur);
        System.out.println(">>> Formateur enregistré avec id : " + savedFormateur.getId());

        return savedFormateur;
    }

    @GetMapping("/{id}")
    public Formateur getFormateurById(@PathVariable Long id) {
        return formateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Formateur non trouvé"));
    }

    @PostMapping
    public Formateur createFormateur(@RequestBody Formateur formateur) {
        return formateurRepository.save(formateur);
    }


    @PutMapping("/{id}")
    public Formateur updateFormateurById(@PathVariable Long id, @RequestBody Formateur formateur) {
        Formateur existing = formateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Formateur non trouvé"));

        existing.setExpertise(formateur.getExpertise());
        existing.setUtilisateur(formateur.getUtilisateur());

        return formateurRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public Formateur deleteFormateurById(@PathVariable Long id) {
        Formateur existing = formateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Formateur non trouvé"));
        formateurRepository.deleteById(id);
        return existing;
    }
}

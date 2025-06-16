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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.group.groupemaker.model.LoginRequest;
import com.group.groupemaker.model.Utilisateur;
import com.group.groupemaker.repository.UtilisateurRepository;
import com.group.groupemaker.service.JwtUtil;

import org.springframework.web.bind.annotation.PutMapping;

@RestController // Gérer les requêtes REST, renvoyer du JSON
@RequestMapping("/utilisateurs") // Toutes les routes commenceront par /utilisateurs
public class UtilisateurController {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UtilisateurController(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = new JwtUtil();
    }

    @GetMapping // On répond à une requête GET avec la liste des utilisateurs
    public List<Utilisateur> getAll() {
        return utilisateurRepository.findAll();
    }

    @PostMapping("/register")
    public Utilisateur register(@RequestBody Utilisateur utilisateur) {
        // Encodage du mot de passe
        String encodedPassword = passwordEncoder.encode(utilisateur.getMotDePasse());
        utilisateur.setMotDePasse(encodedPassword);

        // Optionnel : tu peux aussi fixer le rôle, la date de création, etc.
        utilisateur.setRole("USER");
        utilisateur.setActive(true); // temporaire (on simulera l'activation)

        return utilisateurRepository.save(utilisateur);
    }

    @PostMapping("/register/formateur")
    public Utilisateur registerFormateur(@RequestBody Utilisateur utilisateur) {
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateur.setRole("FORMATEUR");
        utilisateur.setActive(true);
        return utilisateurRepository.save(utilisateur);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail()) // fait une requête SQL pour le
                                                                                        // chercher + récupère l’email
                                                                                        // envoyé dans le JSON
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email incorrect"));
        // on vérifie que le mot de passe est correct
        // request.getMotDePasse() est le mot de passe saisi par l’utilisateur
        // utilisateur.getMotDePasse() est le mot de passe hashé en base
        // passwordEncoder.matches(...) compare les deux en tenant compte du hash BCrypt
        if (!passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Mot de passe incorrect");
        }

        String token = jwtUtil.generateToken(utilisateur.getEmail()); // On génère un token JWT avec l’email de
                                                                      // l’utilisateur comme subject
        return ResponseEntity.ok(token); // On retourne une réponse HTTP 200 OK contenant le token en texte brut
    }

    @GetMapping("/{id}")
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
    @PutMapping("/{id}")
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
    @DeleteMapping("/{id}")
    public Utilisateur deleteUtilisateurById(@PathVariable Long id) {
        Utilisateur existing = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));
        utilisateurRepository.deleteById(id);
        return existing;
    }

}
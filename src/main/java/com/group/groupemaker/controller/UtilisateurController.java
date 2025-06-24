package com.group.groupemaker.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.CookieValue;
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
import org.springframework.security.core.context.SecurityContextHolder;
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
        this.jwtUtil = jwtUtil; // on garde la version injectée par Spring
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

        // ✅ Ne pas forcer le rôle s'il est déjà défini
        if (utilisateur.getRole() == null || utilisateur.getRole().isBlank()) {
            utilisateur.setRole("USER");
        }

        utilisateur.setActive(true);
        utilisateur.setDateCreation(LocalDateTime.now());

        return utilisateurRepository.save(utilisateur);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println(">>> Tentative de login avec " + request.getEmail());

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    System.out.println(">>> Email introuvable !");
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email incorrect");
                });

        if (!passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse())) {
            System.out.println(">>> Mot de passe incorrect !");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Mot de passe incorrect");
        }

        String token = jwtUtil.generateToken(utilisateur.getEmail());
        System.out.println(">>> Login réussi, token généré.");
        return ResponseEntity.ok(token);
    }

    /**
     * Renvoie un utilisateur par son id uniquement s’il s’agit de l’utilisateur
     * connecté.
     * Protège l’accès aux données personnelles d’un autre utilisateur.
     */
    @GetMapping("/{id}")
    public Utilisateur getUtilisateurById(@PathVariable Long id) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Utilisateur connectedUser = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur inconnu"));

        if (!connectedUser.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès interdit à un autre profil");
        }

        return connectedUser;
    }

    /**
     * Met à jour les infos de l’utilisateur connecté uniquement.
     * Empêche la modification d’un autre compte.
     */
    @PutMapping("/{id}")
    public Utilisateur putUtilisateurById(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Utilisateur connectedUser = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur inconnu"));

        if (!connectedUser.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès interdit à un autre profil");
        }

        connectedUser.setPrenom(utilisateur.getPrenom());
        connectedUser.setNom(utilisateur.getNom());
        connectedUser.setEmail(utilisateur.getEmail());
        connectedUser.setMotDePasse(utilisateur.getMotDePasse());

        return utilisateurRepository.save(connectedUser);
    }

    /**
     * Supprime le compte de l’utilisateur connecté uniquement.
     * Refuse la suppression d’un autre utilisateur.
     */
    @DeleteMapping("/{id}")
    public Utilisateur deleteUtilisateurById(@PathVariable Long id) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Utilisateur connectedUser = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur inconnu"));

        if (!connectedUser.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès interdit à un autre profil");
        }

        utilisateurRepository.deleteById(id);
        return connectedUser;
    }

    /**
     * Renvoie le profil de l'utilisateur connecté à partir du token JWT.
     * Permet d'identifier l'utilisateur sans passer d'ID explicite.
     */
    @GetMapping("/me")
    public Utilisateur getCurrentUser(@CookieValue(name = "jwt", required = false) String token) {

        if (token == null || !jwtUtil.isTokenValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token manquant ou invalide");
        }

        String email = jwtUtil.extractEmail(token);

        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalide");
        }

        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
    }

}
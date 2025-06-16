package com.group.groupemaker.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

        // Optionnel : tu peux aussi fixer le rôle, la date de création, etc.
        utilisateur.setRole("USER");
        utilisateur.setActive(true); // temporaire (on simulera l'activation)
        utilisateur.setDateCreation(LocalDateTime.now());

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
    @GetMapping("/me") // Déclare une route accessible en GET, à l’URL /utilisateurs/me.
    public Utilisateur getCurrentUser(@RequestHeader("Authorization") String authHeader) { // Cette méthode attend que
                                                                                           // le client envoie un header
                                                                                           // HTTP nommé Authorization

        // Si l’en-tête est manquant ou ne commence pas par "Bearer ", on renvoie une
        // erreur 401 Unauthorized.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token manquant ou invalide");
        }

        String token = authHeader.substring(7); // On enlève "Bearer " pour ne garder que le token brut
        String email = jwtUtil.extractEmail(token); // On utilise JwtUtil pour extraire l’email qui a été encodé dans le
                                                    // token lors de la connexion.

        // Si le token n’est pas décodable (expiré, modifié, etc.), alors email == null
        // → on bloque la requête.
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalide");
        }

        // On récupère l’utilisateur en base de données à partir de l’email trouvé dans
        // le token.
        // Sinon, on renvoie une erreur 404
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
    }

}
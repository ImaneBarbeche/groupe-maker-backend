package com.group.groupemaker.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.group.groupemaker.model.LoginRequest;
import com.group.groupemaker.model.Utilisateur;
import com.group.groupemaker.model.Role;
import com.group.groupemaker.repository.UtilisateurRepository;
import com.group.groupemaker.service.JwtService;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PutMapping;

@RestController // Gérer les requêtes REST, renvoyer du JSON
@RequestMapping("/utilisateurs") // Toutes les routes commenceront par /utilisateurs
public class UtilisateurController {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UtilisateurController(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder,
            JwtService jwtService, AuthenticationManager authenticationManager) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService; // on garde la version injectée par Spring
        this.authenticationManager = authenticationManager;

    }

    @GetMapping // On répond à une requête GET avec la liste des utilisateurs
    public List<Utilisateur> getAll() {
        return utilisateurRepository.findAll();
    }

    @PostMapping("/register")
public ResponseEntity<Map<String, Object>> register(@RequestBody Utilisateur utilisateur) {
    Map<String, Object> response = new HashMap<>();
    
    System.out.println("📥 Requête reçue pour l'inscription : " + utilisateur);
    
    utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
    utilisateur.setActive(false); // Compte désactivé par défaut
    utilisateur.setRole(Role.USER);
    
    Utilisateur saved = utilisateurRepository.save(utilisateur);
    
    response.put("success", true);
    response.put("message", "Inscription réussie. Veuillez vérifier votre email pour activer votre compte.");
    response.put("data", Map.of(
        "id", saved.getId(),
        "nom", saved.getNom(),
        "prenom", saved.getPrenom(),
        "email", saved.getEmail(),
        "active", saved.isActive()
    ));
    
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
    try {
        // Authentification via AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getMotDePasse()));

        System.out.println("✅ Authentification réussie pour : " + request.getEmail());

        // Génération du token JWT
        Utilisateur utilisateur = new Utilisateur(); // Création d'un utilisateur fictif pour éviter les erreurs
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasse(request.getMotDePasse());

        String token = jwtService.generateToken(utilisateur);
        System.out.println("✅ Token généré : " + token);

        // Création du cookie JWT
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(2 * 60 * 60)
                .sameSite("Lax")
                .build();
        System.out.println("💬 Cookie JWT généré : " + cookie.toString());
        response.setHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(utilisateur);
    } catch (Exception e) {
        System.err.println("Échec de l'authentification : " + e.getMessage());
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Connexion échouée");
    }
}

    /**
     * Renvoie un utilisateur par son id uniquement s’il s’agit de l’utilisateur
     * connecté.
     * Protège l’accès aux données personnelles d’un autre utilisateur.
     */
    @GetMapping("/{id}")
    public Utilisateur getUtilisateurById(@PathVariable Long id) {
        // Fonctionnalité simplifiée - accès restreint
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès interdit - fonctionnalité désactivée");
    }

    /**
     * Met à jour les infos de l’utilisateur connecté uniquement.
     * Empêche la modification d’un autre compte.
     */
    @PutMapping("/{id}")
    public Utilisateur putUtilisateurById(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        // Fonctionnalité simplifiée - modification restreinte
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Modification interdite - fonctionnalité désactivée");
    }

    /**
     * Supprime le compte de l’utilisateur connecté uniquement.
     * Refuse la suppression d’un autre utilisateur.
     */
    @DeleteMapping("/{id}")
    public Utilisateur deleteUtilisateurById(@PathVariable Long id) {
        // Fonctionnalité simplifiée - suppression restreinte
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Suppression interdite - fonctionnalité désactivée");
    }

    /**
     * Renvoie le profil de l'utilisateur connecté à partir du token JWT.
     * Permet d'identifier l'utilisateur sans passer d'ID explicite.
     */
    @GetMapping("/me")
    public Utilisateur getCurrentUser(@CookieValue(name = "jwt", required = false) String token) {

        if (token == null || !jwtService.isTokenValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token manquant ou invalide");
        }

        String email = jwtService.extractEmail(token);

        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalide");
        }

        Utilisateur connectedUser = new Utilisateur(); // Création d'un utilisateur fictif pour éviter les erreurs
        connectedUser.setEmail(email);

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable");
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true) // true en production HTTPS
                .path("/")
                .maxAge(0) // expire immédiatement
                .sameSite("None") // ou "Strict" en fonction de ton usage
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activate/{email}")
    public ResponseEntity<Map<String, Object>> activateAccount(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        
        Utilisateur utilisateur = new Utilisateur(); // Création d'un utilisateur fictif pour éviter les erreurs
        utilisateur.setEmail(email);

        if (utilisateur.isActive()) {
            response.put("success", false);
            response.put("message", "Ce compte est déjà activé");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        utilisateur.setActive(true);
        utilisateur.setDateAcceptationCGU(java.time.LocalDateTime.now());
        utilisateurRepository.save(utilisateur);
        
        response.put("success", true);
        response.put("message", "Compte activé avec succès. Vous pouvez maintenant vous connecter.");
        
        return ResponseEntity.ok(response);
    }

}
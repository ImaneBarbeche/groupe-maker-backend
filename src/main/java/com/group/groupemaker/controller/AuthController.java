package com.group.groupemaker.controller;

import java.time.Duration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.group.groupemaker.model.LoginRequest;
import com.group.groupemaker.model.Utilisateur;
import com.group.groupemaker.repository.UtilisateurRepository;
import com.group.groupemaker.service.JwtService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UtilisateurRepository utilisateurRepository;

    public AuthController(JwtService jwtService,
            AuthenticationManager authenticationManager,
            UtilisateurRepository utilisateurRepository) {
        System.out.println(">>> AuthController chargé !");

        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.utilisateurRepository = utilisateurRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        System.out.println(">>> login appelé !");
        System.out.println("email reçu : " + request.getEmail());
        System.out.println("motDePasse reçu : " + request.getMotDePasse());
        // Authentifier l'utilisateur
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Générer le JWT
        Utilisateur user = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String jwt = jwtService.generateToken(user);

        // Créer un cookie HTTP Only
        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(false) // true si HTTPS
                .path("/")
                .maxAge(Duration.ofHours(2))
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        // Réponse
        return ResponseEntity.ok().body("Connexion réussie");
    }
}

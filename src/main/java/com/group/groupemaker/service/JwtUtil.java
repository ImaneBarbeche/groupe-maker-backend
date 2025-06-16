package com.group.groupemaker.service;

import io.jsonwebtoken.*; // librairie JJWT utilisée pour créer, signer et parser des tokens
import org.springframework.stereotype.Service;

import java.util.Date; // pour gérer les dates d’émission et d’expiration du token
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;

import io.jsonwebtoken.security.Keys;
import java.security.Key;

//  Cette classe va être utilisée pour générer des tokens JWT
// Elle sera injectée dans les controllers via @Autowired ou un constructeur
@Service // annotation Spring qui rend cette classe injectable comme un composant métier
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // permet de générer une clé aléatoire
                                                                         // sécurisée compatible avec l’algorithme HS256
    private static final long EXPIRATION = 1000 * 60 * 60; // Durée de validité du token : ici 3600000 ms = 1 heure

    // Crée un token JWT à partir de l’email de l’utilisateur connecté
    // L’email sera stocké dans le champ sub (subject) du token
    public String generateToken(String email) {
        return Jwts.builder() // Jwts.builder() : démarre la création du token
                .setSubject(email) // l'email devient le "subject" du token
                .setIssuedAt(new Date()) // date actuelle
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // expire dans 1h
                .signWith(key, SignatureAlgorithm.HS256) // signature du token avec la clé et l'algorithme HS256
                .compact(); // Génère le token final sous forme de chaîne encodée (le fameux eyJhbGciOi...)
    }

    public String extractEmail(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key) // même clé que celle utilisée pour générer
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject(); // on récupère l’email (stocké dans "subject")
        } catch (Exception e) {
            return null; // si le token est invalide ou expiré
        }
    }

}

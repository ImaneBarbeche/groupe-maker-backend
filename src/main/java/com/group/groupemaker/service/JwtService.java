package com.group.groupemaker.service;

import com.group.groupemaker.model.Utilisateur;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secret); // Décode la clé Base64
    return Keys.hmacShaKeyFor(keyBytes); // Crée une clé sécurisée pour HMAC-SHA256
}

    // 🕒 Durée de validité du token (ex : 2 heures ici)
    private final long expirationTimeMillis = 2 * 60 * 60 * 1000;

    /**
     * Génère un JWT signé à partir des infos d’un utilisateur
     */
    public String generateToken(Utilisateur utilisateur) {
    return Jwts.builder()
            .setSubject(utilisateur.getEmail())
            .claim("id", utilisateur.getId())
            .claim("prenom", utilisateur.getPrenom())
            .claim("nom", utilisateur.getNom())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)) // 2 heures
            .signWith(SignatureAlgorithm.HS256, getSigningKey())
            .compact();
}



    public String extractEmail(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

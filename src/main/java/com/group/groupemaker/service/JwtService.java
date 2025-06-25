package com.group.groupemaker.service;

import com.group.groupemaker.model.Utilisateur;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // 🛡️ Clé secrète pour signer les tokens JWT
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 🕒 Durée de validité du token (ex : 2 heures ici)
    private final long expirationTimeMillis = 2 * 60 * 60 * 1000;

    /**
     * Génère un JWT signé à partir des infos d’un utilisateur
     */
    public String generateToken(Utilisateur utilisateur) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTimeMillis);

        return Jwts.builder()
                .setSubject(utilisateur.getEmail()) // 📧 L'identifiant principal
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey) // 🔐 Signature du token
                .compact();
    }

    public String extractEmail(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
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
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

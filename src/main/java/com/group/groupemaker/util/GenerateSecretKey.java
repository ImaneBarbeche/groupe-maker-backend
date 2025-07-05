package com.group.groupemaker.util;

import io.jsonwebtoken.security.Keys;
import java.util.Base64;

public class GenerateSecretKey {
    public static void main(String[] args) {
        // Génère une clé sécurisée pour HMAC-SHA256
        byte[] keyBytes = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256).getEncoded();
        // Encode la clé en Base64
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Clé secrète encodée en Base64 : " + base64Key);
    }
}
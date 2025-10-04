package com.klasix12.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtProvider {

    private final PublicKey publicKey;
    private final String algorithm = "RSA";

    public JwtProvider(@Value("${jwt.public-key}") String publicKeyBase64) throws Exception {
        this.publicKey = loadPublicKey(publicKeyBase64);
    }

    private PublicKey loadPublicKey(String publicKeyBase64) throws Exception {
        byte[] encoded = Base64.getDecoder().decode(publicKeyBase64);
        KeyFactory kf = KeyFactory.getInstance(algorithm);
        return kf.generatePublic(new X509EncodedKeySpec(encoded));
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractAllClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractId(String token) {
        return extractAllClaims(token).getId();
    }

    public List<String> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);

        return claims.get("roles", List.class);
    }
}

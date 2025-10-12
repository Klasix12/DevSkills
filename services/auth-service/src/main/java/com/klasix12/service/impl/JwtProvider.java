package com.klasix12.service.impl;

import com.klasix12.dto.Constants;
import com.klasix12.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtProvider {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final long accessTokenExpiration = 5 * 60 * 1000 * 1000; // 5 min
    private final long refreshTokenExpiration = 60 * 60 * 24 * 7 * 1000; // 7 days

    public JwtProvider(@Value("${private.key}") String privateKey, @Value("${public.key}") String publicKey) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(privateKey);
        KeyFactory kf = KeyFactory.getInstance(Constants.ALGORITHM);
        this.privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(decoded));

        decoded = Base64.getDecoder().decode(publicKey);
        this.publicKey = kf.generatePublic(new X509EncodedKeySpec(decoded));
    }

    public String generateAccessToken(UserDto user) {
        return generateToken(user, accessTokenExpiration);
    }

    public String generateRefreshToken(UserDto user) {
        return generateToken(user, refreshTokenExpiration);
    }

    public long extractExpirationEpochSeconds(String token) {
        return extractAllClaims(token).getExpiration().toInstant().getEpochSecond();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public UserDto extractUser(String token) {
        Claims claims = extractAllClaims(token);
        return UserDto.builder()
                .id(Long.valueOf(String.valueOf(claims.get("userId")))) // claims.get("userId")
                .email((String) claims.get("email"))
                .username(claims.getSubject())
                .name((String) claims.get("name"))
                .roles((List<String>) claims.get("roles"))
                .build();
    }

    private String generateToken(UserDto user, long expiration) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("roles", user.getRoles())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(privateKey)
                .compact();
    }
}

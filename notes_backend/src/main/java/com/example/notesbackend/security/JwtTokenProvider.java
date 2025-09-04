package com.example.notesbackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility for creating and validating JWT tokens.
 */
@Component
public class JwtTokenProvider {

    @Value("${security.jwt.secret:ChangeThisSecretForProd}")
    private String jwtSecret;

    @Value("${security.jwt.expiration-ms:86400000}") // 1 day default
    private long jwtExpirationMs;

    private Key getSigningKey() {
        // Generate key from secret using HMAC-SHA256
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // PUBLIC_INTERFACE
    public String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    // PUBLIC_INTERFACE
    public String getUsernameFromToken(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // PUBLIC_INTERFACE
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }
}

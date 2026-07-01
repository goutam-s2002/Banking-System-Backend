package com.bankingsystem.util;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final String secret = "mysecretkeymysecretkeymysecretkey123";

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 🔥 TOKEN GENERATE
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getKey())   // ✅ FIXED
                .compact();
    }

    // 🔥 USERNAME EXTRACT
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // 🔥 ROLE EXTRACT
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // 🔥 COMMON METHOD
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
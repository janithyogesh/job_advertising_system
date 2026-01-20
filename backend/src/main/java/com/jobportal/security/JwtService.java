package com.jobportal.security;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jobportal.model.UserAccount;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey key;
    private final Duration expiration;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration}") Duration expiration
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    // ================= TOKEN GENERATION =================
    public String generateToken(UserAccount user) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration.toMillis());

        return Jwts.builder()
                .subject(user.getEmail())          // ✔ new API
                .claim("role", user.getRole().name())
                .claim("fullName", user.getFullName())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)                     // ✔ algorithm inferred
                .compact();
    }

    // ================= TOKEN PARSING =================
    public Claims parseClaims(String token) {

        return Jwts.parser()        // ✅ CORRECT for jjwt 0.12.x
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

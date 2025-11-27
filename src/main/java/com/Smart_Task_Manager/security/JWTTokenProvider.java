package com.Smart_Task_Manager.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTTokenProvider {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger("detailedLogger");

    private final SecretKey secretKey;
    private final long expiration;

    public JWTTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expiration
    ) {
        // Decode Base64 and convert to SecretKey
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expiration = expiration;
    }

    // -------------- 1️⃣ Helper: Extract User role safely ----------------
    private String extractRole(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .findFirst()
                .map(auth -> auth.getAuthority())
                .orElse("ROLE_USER");
    }

    // -------------- 2️⃣ Generate JWT Token ----------------
    public String generateToken(UserDetails userDetails) {
        String role = extractRole(userDetails);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // -------------- 3️⃣ Helper: Remove Bearer Prefix ----------------
    public String resolveToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);  // remove "Bearer "
        }
        return token;
    }

    // -------------- Extract username from token ----------------
    public String getUsernameFromToken(String token) {
        token = resolveToken(token);

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // -------------- Validate Token with Detailed Logs ----------------
    public boolean validateToken(String token) {
        try {
            token = resolveToken(token);

            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            return true;

        } catch (Exception ex) {
            logger.error("JWT validation failed: {}", ex.getMessage());
            return false;
        }
    }
}

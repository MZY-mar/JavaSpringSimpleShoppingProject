package com.beaconfire.project22.JwtUtil;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${security.jwt.token.key}")
    private String key;

    private SecretKey getSigningKey() {
        // Convert the key string into a byte array and return a SecretKey
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    // Create JWT from a UserDetail
    public String generateToken(String username, int role) {
        String roleName = role == 1 ? "ROLE_ADMIN" : "ROLE_USER";

        return Jwts.builder()
                .setSubject(username)
                .claim("role", roleName)  // Store role in JWT
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // 10-hour expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // Use the signing key and algorithm
                .compact();
    }

    // Validate the JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())  // Use the updated parser
                    .build()
                    .parseClaimsJws(token);  // This will throw an exception if the token is invalid
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extract role from the token
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }
}

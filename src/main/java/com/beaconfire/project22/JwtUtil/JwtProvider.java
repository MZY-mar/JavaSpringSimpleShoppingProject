package com.beaconfire.project22.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    @Value("${security.jwt.token.key}")
    private String key;


    // create jwt from a UserDetail
    public String generateToken(String username, int role) {
        String roleName = role == 1 ? "ROLE_ADMIN" : "ROLE_USER";

        return Jwts.builder()
                .setSubject(username)
                .claim("role", roleName)  // 将角色存入 JWT
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // 10小时有效期
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getRoleFromToken(String token) {
        Claims claims =
                Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims.get("role", String.class);
    }

}

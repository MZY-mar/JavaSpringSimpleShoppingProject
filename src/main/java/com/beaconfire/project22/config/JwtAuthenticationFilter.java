package com.beaconfire.project22.config;
import com.beaconfire.project22.JwtUtil.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Value("${security.jwt.token.key}")
    private String key;

    private SecretKey getSigningKey() {
        // Convert the key string into a byte array and return a SecretKey
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = resolveToken(request);

        if (jwt != null && validateToken(jwt)) {
            String role = getRoleFromToken(jwt);
            String username = "user";
            System.out.println("role for this request" + role);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null,
                            Collections.singletonList(new SimpleGrantedAuthority(role)));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer")) {

            System.out.println(bearerToken+" this is the token");
           return bearerToken.substring(7);
        }
        return null;
    }

    private boolean validateToken(String token) {
        return true;
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Skip JWT filter for login endpoint
        String path = request.getRequestURI();
        return path.equals("/users/login") || path.equals("/users/register");
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }

}

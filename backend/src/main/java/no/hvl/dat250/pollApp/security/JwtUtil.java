package no.hvl.dat250.pollApp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    // Secret key for signing the JWT, should be stored securely (e.g., in environment variables)
    private final SecretKey key = Jwts.SIG.HS256.key().build();

    // JWT expiration time (10 hours)
    private long expirationTime = 1000 * 60 * 60 * 10;

    // Generate a JWT token
    public String generateToken(String username, Collection<? extends GrantedAuthority> roles) {
        List<String> roleNames = roles.stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role) // Ensure ROLE_ prefix
                .toList();

        return Jwts.builder()
                .subject(username)
                .claim("roles", roleNames) // Add roles to token
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    // Extract claims from token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key) // Use the signing key
                .build()
                .parseClaimsJws(token) // Parse the token
                .getBody(); // Get the claims
    }

    // Extract username from token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extract roles
    public List<String> extractRoles(String token) {
        return (List<String>) extractAllClaims(token).get("roles");
    }

    // Validate token
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Check token expiration
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}

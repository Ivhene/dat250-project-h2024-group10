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
        System.out.println("Generating token");
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    // Extract the username from the token
    public String extractUsername(String token) {
        try {
            Claims claims = Jwts.parser() // Create a parser builder
                    .verifyWith(key) // Set the signing key (same key used for signing the token)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload(); // Get the claims body

            return claims.getSubject(); // Get and return the subject (username)
        } catch (Exception e) {
            System.err.println("Error extracting username: " + e.getMessage());
            return null; // or throw a custom exception
        }
    }

    // Validate if the token is expired
    public boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    // Get the expiration date from the token
    public Date getExpirationDate(String token) {
        try {
            Claims claims = Jwts.parser() // Create a parser builder
                    .verifyWith(key) // Set the signing key (same key used for signing the token)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload(); // Get the claims body

            return claims.getExpiration(); // Get and return the subject (username)
        } catch (Exception e) {
            System.err.println("Error getting Expiration Date: " + e.getMessage());
            return null; // or throw a custom exception
        }
    }

    // Validate the token with respect to the user
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    public List<String> extractRoles(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseUnsecuredClaims(token)
                .getPayload();

        return (List<String>) claims.get("roles");
    }

}

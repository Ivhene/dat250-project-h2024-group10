package no.hvl.dat250.pollApp.controller;

import no.hvl.dat250.pollApp.entity.User;
import no.hvl.dat250.pollApp.repo.DomainManager;
import no.hvl.dat250.pollApp.security.AuthRequest;
import no.hvl.dat250.pollApp.security.AuthResponse;
import no.hvl.dat250.pollApp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@RestController
public class AuthController {

    @Autowired
    private DomainManager domainManager;


    // Endpoint for user login and JWT token generation
    @PostMapping("/api/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {

            User user = domainManager.getUserById(authRequest.getUsername());
            if(user != null) {
                return ResponseEntity.badRequest().body("User already exists");
            }

            // Authenticate the user
            AuthResponse token = domainManager.authenticate(authRequest);

            return ResponseEntity.ok(token); // Send token back in response
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }


}

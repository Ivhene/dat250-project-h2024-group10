package no.hvl.dat250.pollApp.controller;

import no.hvl.dat250.pollApp.security.AuthRequest;
import no.hvl.dat250.pollApp.security.AuthResponse;
import no.hvl.dat250.pollApp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
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
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;


    // Endpoint for user login and JWT token generation
    @PostMapping("/api/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            // Authenticate the user
            AuthResponse token = authenticate(authRequest);

            return ResponseEntity.ok(token); // Send token back in response
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    public AuthResponse authenticate(AuthRequest authRequest) {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            String token;
            UserDetails userDetails;
            // Load user details
            try {
                 userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
                 token = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
            } catch (UsernameNotFoundException e) {
                userDetails = User
                        .withUsername(authRequest.getUsername())
                        .password(authRequest.getPassword())
                        .authorities("USER", "ADMIN") // Remove "ROLE_" prefix here
                        .build();


                // Add the user to the UserDetailsService (only works with InMemoryUserDetailsManager)
                if (userDetailsService instanceof InMemoryUserDetailsManager) {
                    ((InMemoryUserDetailsManager) userDetailsService).createUser(userDetails);
                } else {
                    throw new RuntimeException("UserDetailsService does not support adding new users");
                }
                token = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
            }

            // Generate the JWT token


            return new AuthResponse(token);
    }
}

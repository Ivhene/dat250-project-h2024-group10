package no.hvl.dat250.pollApp.security;

import no.hvl.dat250.pollApp.controller.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AuthIntegrationTest {

    private AuthRequest authRequest;
    private AuthResponse authResponse;
    private JwtUtil jwtUtil;
    private JwtUtil jwtUtil2;
    private AuthController authController;
    private List roles = new ArrayList();


    @BeforeEach
    void setUp() {
        // Mock dependencies
        jwtUtil2 = new JwtUtil();
        jwtUtil = mock(JwtUtil.class);
        authController = new AuthController();

        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");
        // Create test objects
        authRequest = new AuthRequest("testUser", "testPassword");

        authResponse = new AuthResponse("testToken");
    }

    @Test
    void testAuthorizeUrlsSecurityConfig() {
        AuthorizeUrlsSecurityConfig securityConfig = new AuthorizeUrlsSecurityConfig();
        assertNotNull(securityConfig, "Security config should not be null");
    }

    @Test
    void testAuthRequest() {
        assertEquals("testUser", authRequest.getUsername());
        assertEquals("testPassword", authRequest.getPassword());
    }

    @Test
    void testAuthResponse() {
        assertEquals("testToken", authResponse.getToken());
    }

    @Test
    void testJwtUtilGenerateToken() {
        when(jwtUtil.generateToken("testUser", roles)).thenReturn("mockToken");

        String token = jwtUtil.generateToken("testUser", roles);
        assertEquals("mockToken", token);

        verify(jwtUtil, times(1)).generateToken("testUser", roles);
    }

    @Test
    void testExtractUsername() {
        // Arrange: Generate a valid JWT token
        String token = jwtUtil2.generateToken("testUser", roles);

        // Act: Extract the username from the token
        String extractedUsername = jwtUtil2.extractUsername(token);

        // Assert: Verify the extracted username matches the input
        assertEquals("testUser", extractedUsername, "The extracted username does not match the expected value");
    }

    @Test
    void testExtractExpirationDate() {
        // Arrange: Generate a token with a short expiration time
        long expirationTimeMs = 1000 * 60 * 60 * 10;
        String token = jwtUtil2.generateToken("testUser", roles);

        // Act: Extract the expiration date from the token
        Date expirationDate = jwtUtil2.getExpirationDate(token);

        // Assert: Verify the expiration date is close to the expected value
        long expectedExpirationTime = System.currentTimeMillis() + expirationTimeMs;
        assertTrue(expirationDate.getTime() - expectedExpirationTime < 1000, "Expiration date is not within expected range");
    }




}

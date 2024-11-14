package no.hvl.dat250.pollApp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.hvl.dat250.pollApp.controller.AuthController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(AuthorizeUrlsSecurityConfig.class)
public class AuthTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    public void testLoginWithValidCredentials() throws Exception {
        AuthRequest authRequest = new AuthRequest("user", "password");
        String token = "mocked-jwt-token";

        UserDetails userDetails = User.withUsername("user")
                .password("{noop}password") // {noop} disables password encoding
                .roles("USER", "ADMIN")
                .build();

        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenReturn(null);
        Mockito.when(userDetailsService.loadUserByUsername("user"))
                .thenReturn(userDetails);
        Mockito.when(jwtUtil.generateToken(Mockito.anyString(), Mockito.anyCollection()))
                .thenReturn(token);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    public void testLoginWithInvalidCredentials() throws Exception {
        AuthRequest authRequest = new AuthRequest("user", "wrong-password");

        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized());
    }
}

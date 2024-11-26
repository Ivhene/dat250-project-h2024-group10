package no.hvl.dat250.pollApp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AuthorizeUrlsSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF for testing purposes
                .httpBasic().disable() // Disable Basic Auth if not needed
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        // Restrict specific endpoints
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/polls/**").hasAuthority("USER")
                        .requestMatchers("/users/**").hasAuthority("USER")
                        // Allow all other endpoints
                        .anyRequest().permitAll()
                )
                .formLogin().disable(); // Disable form-based login
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Create a default user for testing purposes
        UserDetails anonymous = User.withDefaultPasswordEncoder()
                .username("anonymous")
                .password("password")
                .roles("USER") // Assign "USER" role
                .build();
        return new InMemoryUserDetailsManager(anonymous);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

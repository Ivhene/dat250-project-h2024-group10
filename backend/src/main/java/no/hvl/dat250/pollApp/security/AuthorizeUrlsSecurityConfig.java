package no.hvl.dat250.pollApp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class AuthorizeUrlsSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/api/auth/login").permitAll()
                                .requestMatchers("/**").permitAll()
                                .anyRequest().authenticated()// Allow login endpoint without authentication
                )
                .formLogin(withDefaults())
                .httpBasic().disable()
                .csrf().disable();
        return http. build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("ROLE_USER")
                .build();
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("password")
                .roles("ROLE_ADMIN", "ROLE_USER")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}


// withDefaultPasswordEncoder()
// Using this method is not considered safe for production,
// but is acceptable for demos and getting started.
// For production purposes, ensure the password is encoded externally.
// See the method Javadoc for additional details.
// There are no plans to remove this support.
// It is deprecated to indicate that this is considered insecure for production purposes

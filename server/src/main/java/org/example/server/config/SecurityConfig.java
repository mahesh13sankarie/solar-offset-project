package org.example.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        // Permit all requests (public endpoints)
                        .requestMatchers("/public/**").permitAll()
                        // Secure all other endpoints with authentication
                        .anyRequest().authenticated()
                )
                // OAuth2 Login (used for authentication)
                .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/generatetoken"))
                // Logout Handling
                .logout(logout -> logout.logoutSuccessUrl("/logout"));

        return http.build();
    }
}
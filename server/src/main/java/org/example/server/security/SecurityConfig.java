package org.example.server.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //TODO: adjust URL
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((authorize) ->
                        authorize.anyRequest().authenticated()
                )
                .oauth2Login((oauth2) -> oauth2.defaultSuccessUrl("/generatetoken"))
                .logout((logout) -> logout.logoutSuccessUrl("/logout"))
                .build();
    }
}

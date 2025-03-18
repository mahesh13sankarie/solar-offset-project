package org.example.server.config;

import org.example.server.mapper.AuthResponseMapper;
import org.example.server.mapper.UserMapper;
import org.example.server.utils.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //Todo: add missing security config for google auth~ check MR!
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/*").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login((oauth2) -> oauth2.defaultSuccessUrl("/api/v1/auth/generatetoken"))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        ;
        return http.build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //TODO: move!
    @Bean
    UserMapper userMapper() {
        return new UserMapper();
    }

    @Bean
    TokenProvider tokenProvider() {
        return new TokenProvider();
    }

    @Bean
    JwtDecoder decoder(){
        SecretKey secretKey = new SecretKeySpec("SyntaxSquadSecretKeySheffield20242025".getBytes(), "HMAC256"); //move to constant
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    AuthResponseMapper authResponseMapper() { return new AuthResponseMapper(); }
}
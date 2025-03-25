package org.example.server.config;

import org.example.server.utils.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author: astidhiyaa
 * @date: 18/03/25
 */
@Configuration
public class TokenConfig {
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
}

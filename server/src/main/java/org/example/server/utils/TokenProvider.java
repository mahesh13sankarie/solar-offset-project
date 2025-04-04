package org.example.server.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.example.server.entity.User;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: astidhiyaa
 * @date: 12/03/25
 */
@Component
public class TokenProvider {
    //TODO: use annotation @Value?
    private final String secret_key = "SyntaxSquadSecretKeySheffield20242025";
    private final Algorithm algorithm = Algorithm.HMAC256(secret_key);
    private final int expires_in = 3600 * 60;

    public String generateToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withClaim("email", email)
                .withExpiresAt(new Date(System.currentTimeMillis() + expires_in * 1000))
                .sign(algorithm);
    }
}

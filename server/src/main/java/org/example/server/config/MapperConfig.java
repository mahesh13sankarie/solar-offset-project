package org.example.server.config;

import org.example.server.mapper.AuthResponseMapper;
import org.example.server.mapper.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: astidhiyaa
 * @date: 18/03/25
 */
@Configuration
public class MapperConfig {
    @Bean
    UserMapper userMapper() {
        return new UserMapper();
    }

    @Bean
    AuthResponseMapper authResponseMapper() { return new AuthResponseMapper(); }
}

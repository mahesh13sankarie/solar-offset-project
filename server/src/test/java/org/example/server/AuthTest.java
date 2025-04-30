package org.example.server;

import org.example.server.service.auth.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: astidhiyaa
 * @date: 30/04/25
 */
@SpringBootTest
public class AuthTest {
    @Autowired
    private AuthServiceImpl userDetailsService;

    //testing purpose
    @Test
    void testUserHasAdminRole() {
        UserDetails user = userDetailsService.loadUserByUsername("admin@gmail.com");
        assertTrue(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}

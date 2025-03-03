package org.example.server.controller;

import org.example.server.dto.UserDto;
import org.example.server.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 **
 * Google login path: <a href="http://localhost:8000/login/oauth2/code/google">...</a>
 */

@RestController
public class AuthController {
    private UserRepository userRepository;

    /*TODO: adjust mapping
    @PostMapping("/signup")
    ResponseEntity<?> signup(@RequestBody UserDto userDto){
        return ResponseEntity.ok("200");
    }
    */
}

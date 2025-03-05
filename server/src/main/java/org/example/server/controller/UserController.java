package org.example.server.controller;
import org.example.server.entity.User;
import org.example.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/register")
@CrossOrigin(origins = "http://127.0.0.1:5500") // Allow frontend to connect
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public Map<String, Object> registerUser(@RequestBody User user) {
        userRepository.save(user); // Save to database

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User registered successfully");
        return response;
    }
}
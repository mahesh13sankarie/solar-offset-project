package org.example.server.controller;
import org.example.server.entity.User;
import org.example.server.repository.UserRepository;
import org.example.server.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api")
@CrossOrigin
//@CrossOrigin(origins = "http://127.0.0.1:5500") // Allow frontend to connect
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {

        try {
            userService.registerUser(user);
            return "success user registered";
        }
        catch (Exception e) {
            return "Error: " +  e.getMessage();
        }
    }
}
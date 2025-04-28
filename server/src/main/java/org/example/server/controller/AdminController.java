package org.example.server.controller;

import org.example.server.dto.UserRequest;
import org.example.server.entity.User;
import org.example.server.mapper.AuthResponseMapper;
import org.example.server.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: astidhiyaa
 * @date: 25/03/25
 */
@RequestMapping("api/v1/dashboard")
@RestController
@CrossOrigin
public class AdminController {
    @Autowired
    private AuthService authService;

    @Autowired
    AuthResponseMapper responseMapper;

    //Fetch all user data except admin
    @GetMapping("/users")
    public ResponseEntity<?> fetchUser() {
        List<User> users = authService.getUsers();
        if (users.isEmpty()) {
            return ResponseEntity.ok().body(responseMapper.buildErrorMessage("Empty user", 400));
        } else {
            return ResponseEntity.ok().body(responseMapper.buildCustomResponse(users));
        }
    }

    @PutMapping("/update-role")
    public ResponseEntity<?> updateRole(@RequestBody UserRequest userRequest) {
        authService.updateRole(userRequest);
        return ResponseEntity.ok().body(responseMapper.buildSuccessResponse());
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestBody UserRequest userRequest) {
        authService.deleteUser(userRequest.userId());
        return ResponseEntity.ok().body(responseMapper.buildSuccessResponse());
    }
}

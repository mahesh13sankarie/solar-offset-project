package org.example.server.controller;

import org.example.server.dto.UserRequest;
import org.example.server.entity.User;
import org.example.server.mapper.AuthResponseMapper;
import org.example.server.service.auth.AuthService;
import org.example.server.utils.ApiResponse;
import org.example.server.utils.ApiResponseGenerator;
import org.example.server.utils.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    // Fetch all user data except admin
    @GetMapping("/users")
    public ApiResponse<ApiResponse.CustomBody<List<User>>> fetchUser() {
        try {
            List<User> users = authService.getUsers();
            if (users.isEmpty()) {
                return failForUserList("EMPTY_USER_LIST", "No users found", HttpStatus.OK);
            } else {
                return ApiResponseGenerator.success(HttpStatus.OK, users);
            }
        } catch (Exception e) {
            return failForUserList("SERVER_ERROR", "Failed to fetch users", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-role")
    public ApiResponse<ApiResponse.CustomBody<Object>> updateRole(@RequestBody UserRequest userRequest) {
        try {
            validateUserRequest(userRequest);
            authService.updateRole(userRequest);
            return ApiResponseGenerator.success(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ApiResponseGenerator.fail("VALIDATION_ERROR", e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseGenerator.fail("SERVER_ERROR", "Failed to update user role",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-user")
    public ApiResponse<ApiResponse.CustomBody<Object>> deleteUser(@RequestBody UserRequest userRequest) {
        try {
            validateUserRequest(userRequest);
            authService.deleteUser(userRequest.userId());
            return ApiResponseGenerator.success(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ApiResponseGenerator.fail("VALIDATION_ERROR", e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseGenerator.fail("SERVER_ERROR", "Failed to delete user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method for creating typed error responses for user list
    private ApiResponse<ApiResponse.CustomBody<List<User>>> failForUserList(String code, String message,
            HttpStatus status) {
        return new ApiResponse<>(
                new ApiResponse.CustomBody<>(false, null, new Error(code, message, status.toString())),
                status);
    }

    // Validate user request
    private void validateUserRequest(UserRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("User request must not be null");
        }
        if (request.userId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
    }
}

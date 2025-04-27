//package org.example.server.controller;
//
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
//import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.json.gson.GsonFactory;
//import org.example.server.dto.GoogleSignInDTO;
//import org.example.server.entity.User;
//import org.example.server.service.auth.AuthService;
//import org.example.server.repository.UserRepository;
//import org.example.server.utils.TokenProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.GeneralSecurityException;
//import java.util.Collections;
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/api/v1/auth")
//@CrossOrigin(origins = "http://localhost:5173")
//public class AuthController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private AuthService authService;
//
//    @Autowired
//    private TokenProvider tokenProvider;
//
//    private static final String CLIENT_ID = "981770573082-avncdn59tmpu735gaofksnp9ijvhle53.apps.googleusercontent.com";
//    @PostMapping("/google-login")
//    public ResponseEntity<?> googleLogin(@RequestBody GoogleSignInDTO tokenRequest) throws GeneralSecurityException, IOException {
//        String idTokenString = tokenRequest.getToken();
//
//        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
//                new GooglePublicKeysManager.Builder(
//                        GoogleNetHttpTransport.newTrustedTransport(),
//                        GsonFactory.getDefaultInstance())
//                        .setAudience(Collections.singletonList(CLIENT_ID))
//                        .build())
//                .build();
//
//        GoogleIdToken idToken = verifier.verify(idTokenString);
//
//        if (idToken != null) {
//            GoogleIdToken.Payload payload = idToken.getPayload();
//
//            String email = payload.getEmail();
//            String name = (String) payload.get("name");
//            boolean emailVerified = Boolean.TRUE.equals(payload.getEmailVerified());
//
//            if (!emailVerified) {
//                return ResponseEntity.badRequest().body("Email not verified by Google.");
//            }
//
//            // Check if user exists
//            User user = userRepository.findByEmail(email);
//            if (user == null) {
//                // Register the new user
//                user = new User();
//                user.setEmail(email);
//                user.setFullName(name);
//                user.setPassword(""); // no password since Google account
//                user.setAccountType(1); // assuming 1 = Google login
//                userRepository.save(user);
//            }
//
//            // Generate our app's token
//            String token = tokenProvider.generateToken(user.getEmail());
//
//            // Return user info + token
//            return ResponseEntity.ok().body(
//                    Map.of(
//                            "token", token,
//                            "data", user
//                    )
//            );
//
//        } else {
//            return ResponseEntity.badRequest().body("Invalid ID Token.");
//        }
//    }
//}

package com.apilimiter.controller;

import com.apilimiter.dto.AuthDto;
import com.apilimiter.model.User;
import com.apilimiter.security.JwtUtil;
import com.apilimiter.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody AuthDto.RegisterRequest request) {
        try {
            User user = userService.registerUser(request.getEmail(), request.getPassword());
            String token = jwtUtil.generateJwtToken(user.getEmail());

            AuthDto.AuthResponse response = new AuthDto.AuthResponse(
                token, 
                user.getApiKey(), 
                user.getEmail()
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthDto.LoginRequest request) {
        Optional<User> userOpt = userService.authenticateUser(request.getEmail(), request.getPassword());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String token = jwtUtil.generateJwtToken(user.getEmail());

            AuthDto.AuthResponse response = new AuthDto.AuthResponse(
                token, 
                user.getApiKey(), 
                user.getEmail()
            );

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Error: Invalid email or password!");
        }
    }

    @PostMapping("/generate-key")
    public ResponseEntity<?> generateNewApiKey(Authentication authentication) {
        Optional<User> userOpt = userService.findByEmail(authentication.getName());

        if (userOpt.isPresent()) {
            String newApiKey = userService.generateNewApiKey(userOpt.get());
            return ResponseEntity.ok().body("{\"apiKey\":\"" + newApiKey + "\"}");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
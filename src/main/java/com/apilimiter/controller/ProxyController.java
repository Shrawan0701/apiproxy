package com.apilimiter.controller;

import com.apilimiter.dto.ProxyDto;
import com.apilimiter.model.User;
import com.apilimiter.service.ProxyService;
import com.apilimiter.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProxyController {

    @Autowired
    private ProxyService proxyService;

    @Autowired
    private UserService userService;

    @PostMapping("/proxy")
    public ResponseEntity<?> proxyRequest(
            @RequestHeader("X-API-Key") String apiKey,
            @Valid @RequestBody ProxyDto.ProxyRequest request) {

        Optional<User> userOpt = userService.findByApiKey(apiKey);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid API key");
        }

        ProxyDto.ProxyResponse response = proxyService.forwardRequest(userOpt.get(), request);

        if (response.getError() != null && response.getStatusCode() == 429) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
        }

        return ResponseEntity.ok(response);
    }
}
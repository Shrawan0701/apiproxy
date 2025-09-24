package com.apilimiter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDto {

    public static class LoginRequest {
        @NotBlank
        @Email
        private String email;

        @NotBlank
        @Size(min = 6, max = 100)
        private String password;

        public LoginRequest() {}

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RegisterRequest {
        @NotBlank
        @Email
        private String email;

        @NotBlank
        @Size(min = 6, max = 100)
        private String password;

        public RegisterRequest() {}

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AuthResponse {
        private String token;
        private String apiKey;
        private String email;

        public AuthResponse(String token, String apiKey, String email) {
            this.token = token;
            this.apiKey = apiKey;
            this.email = email;
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }

        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}
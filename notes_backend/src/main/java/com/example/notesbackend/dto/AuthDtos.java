package com.example.notesbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Authentication request/response DTOs.
 */
public class AuthDtos {

    public static class RegisterRequest {
        @Schema(description = "Unique username", example = "jdoe")
        @NotBlank @Size(min = 3, max = 50)
        public String username;

        @Schema(description = "User email", example = "jdoe@example.com")
        @Email @NotBlank
        public String email;

        @Schema(description = "Password, min 8 chars", example = "StrongPass123")
        @NotBlank @Size(min = 8, max = 120)
        public String password;
    }

    public static class LoginRequest {
        @Schema(description = "Username", example = "jdoe")
        @NotBlank
        public String username;

        @Schema(description = "Password", example = "StrongPass123")
        @NotBlank
        public String password;
    }

    public static class AuthResponse {
        @Schema(description = "JWT Bearer token")
        public String token;

        @Schema(description = "Username")
        public String username;

        public AuthResponse(String token, String username) {
            this.token = token;
            this.username = username;
        }
    }
}

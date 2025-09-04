package com.example.notesbackend.controller;

import com.example.notesbackend.dto.AuthDtos;
import com.example.notesbackend.security.JwtTokenProvider;
import com.example.notesbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication endpoints for registration and login.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User registration and login")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService,
                          JwtTokenProvider jwtTokenProvider,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    // PUBLIC_INTERFACE
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a user account with username, email, and password.")
    public ResponseEntity<?> register(@Valid @RequestBody AuthDtos.RegisterRequest req) {
        var user = userService.register(req.username, req.email, req.password);
        String token = jwtTokenProvider.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthDtos.AuthResponse(token, user.getUsername()));
    }

    // PUBLIC_INTERFACE
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate and obtain a JWT token.")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDtos.LoginRequest req) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.username, req.password));
            String token = jwtTokenProvider.generateToken(req.username);
            return ResponseEntity.ok(new AuthDtos.AuthResponse(token, req.username));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}

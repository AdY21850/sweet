package com.example.sweet.controller;

import com.example.sweet.dto.GoogleTokenRequest;
import com.example.sweet.dto.LoginRequest;
import com.example.sweet.dto.LoginResponse;
import com.example.sweet.dto.RegisterRequest;
import com.example.sweet.model.User;
import com.example.sweet.service.UserService;
import com.example.sweet.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(
        origins = "https://sweet-shop-manager-rho.vercel.app",
        allowCredentials = "true"
)
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // ==========================
    // ✅ EMAIL REGISTER
    // ==========================
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            String token = jwtUtil.generateToken(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new LoginResponse(true, "User registered successfully", token, user)
            );

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new LoginResponse(false, ex.getMessage(), null, null)
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new LoginResponse(false, "Server error during registration", null, null)
            );
        }
    }

    // ==========================
    // ✅ EMAIL LOGIN
    // ==========================
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            boolean success = userService.login(request.getEmail(), request.getPassword());

            if (!success) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new LoginResponse(false, "Invalid credentials", null, null)
                );
            }

            User user = userService.getByEmail(request.getEmail());
            String token = jwtUtil.generateToken(user);

            return ResponseEntity.ok(
                    new LoginResponse(true, "Login successful", token, user)
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new LoginResponse(false, "Login failed due to server error", null, null)
            );
        }
    }

    // ==========================
    // ✅ GOOGLE LOGIN
    // ==========================

    @PostMapping("/google-login")
    public ResponseEntity<LoginResponse> googleLogin(@RequestBody GoogleTokenRequest request) {
        try {
            User user = userService.loginWithGoogle(request.getToken(), false);
            String jwt = jwtUtil.generateToken(user);

            return ResponseEntity.ok(
                    new LoginResponse(true, "Google login successful", jwt, user)
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new LoginResponse(false, "Google login failed: " + e.getMessage(), null, null)
            );
        }
    }

    // ==========================
    // ✅ GOOGLE REGISTER
    // ==========================

    @PostMapping("/google-register")
    public ResponseEntity<LoginResponse> googleRegister(@RequestBody GoogleTokenRequest request) {
        try {
            User user = userService.loginWithGoogle(request.getToken(), true);
            String jwt = jwtUtil.generateToken(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new LoginResponse(true, "Google registration successful", jwt, user)
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new LoginResponse(false, "Google registration failed: " + e.getMessage(), null, null)
            );
        }
    }
}

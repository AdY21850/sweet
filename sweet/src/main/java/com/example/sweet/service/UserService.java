package com.example.sweet.service;

import com.example.sweet.dto.RegisterRequest;
import com.example.sweet.model.Role;
import com.example.sweet.model.User;
import com.example.sweet.repository.UserRepository;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 🔴 TEMP: HARD-CODED GOOGLE WEB CLIENT ID (FOR DEBUGGING)
    private static final String GOOGLE_WEB_CLIENT_ID =
            "478331272752-q7kqaidvmamv9llv1e796708mpa4f485.apps.googleusercontent.com";

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ==========================
    // ✅ EMAIL REGISTER
    // ==========================
    public User register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setFullName(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    // ==========================
    // ✅ GET USER
    // ==========================
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    // ==========================
    // ✅ EMAIL LOGIN
    // ==========================
    public boolean login(String email, String rawPassword) {
        User user = getByEmail(email);

        if (user.getPassword() == null) return false;

        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    // ==========================
    // ✅ GOOGLE LOGIN / REGISTER ENGINE (OFFICIAL + STABLE)
    // ==========================
    public User loginWithGoogle(String googleToken, boolean forceRegister) {

        try {
            System.out.println("VERIFYING GOOGLE TOKEN WITH CLIENT ID = " + GOOGLE_WEB_CLIENT_ID);

            GoogleIdTokenVerifier verifier =
                    new GoogleIdTokenVerifier.Builder(
                            GoogleNetHttpTransport.newTrustedTransport(),
                            GsonFactory.getDefaultInstance()
                    )
                            .setAudience(Collections.singletonList(GOOGLE_WEB_CLIENT_ID))
                            .build();

            GoogleIdToken idToken = verifier.verify(googleToken);

            if (idToken == null) {
                throw new IllegalArgumentException("Invalid Google token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            String email = payload.getEmail();
            String name = (String) payload.get("name");

            if (email == null) {
                throw new IllegalArgumentException("Google token missing email");
            }

            // 🔹 Check Existing User
            User existingUser = userRepository.findByEmail(email).orElse(null);

            // ❌ Register but user exists
            if (forceRegister && existingUser != null) {
                throw new IllegalArgumentException("User already exists. Please login.");
            }

            // ❌ Login but user missing
            if (!forceRegister && existingUser == null) {
                throw new IllegalArgumentException("User not registered. Please sign up.");
            }

            // ✅ Existing user
            if (existingUser != null) {
                return existingUser;
            }

            // ✅ Create new Google user
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(name);
            newUser.setFullName(name);
            newUser.setPassword(null);
            newUser.setRole(Role.USER);

            return userRepository.save(newUser);

        } catch (Exception e) {
            throw new IllegalArgumentException("Google authentication failed: " + e.getMessage());
        }
    }
}

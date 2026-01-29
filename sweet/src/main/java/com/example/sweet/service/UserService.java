package com.example.sweet.service;

import com.example.sweet.dto.RegisterRequest;
import com.example.sweet.model.Role;
import com.example.sweet.model.User;
import com.example.sweet.repository.UserRepository;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${google.client.id}")
    private String googleClientId;

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
    // ✅ GOOGLE LOGIN / REGISTER ENGINE (FINAL WORKING VERSION)
    // ==========================
    public User loginWithGoogle(String googleToken, boolean forceRegister) {

        try {
            // 🔹 Call Google TokenInfo API
            String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + googleToken;

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                throw new IllegalArgumentException("Invalid Google token");
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            JSONObject payload = new JSONObject(response.toString());

            // 🔹 Validate Audience
            String audience = payload.optString("aud", "");
            if (!audience.equals(googleClientId)) {
                throw new IllegalArgumentException("Google token audience mismatch");
            }

            // 🔹 Extract Data
            String email = payload.optString("email", null);
            String name = payload.optString("name", "Google User");

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
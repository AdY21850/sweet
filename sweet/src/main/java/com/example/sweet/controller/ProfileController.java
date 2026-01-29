package com.example.sweet.controller;

import com.example.sweet.model.User;
import com.example.sweet.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ==========================
    // ✅ GET LOGGED-IN USER PROFILE
    // ==========================
    @GetMapping
    public User getProfile(Authentication authentication) {
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ==========================
    // ✅ UPDATE PROFILE (NAME + PHONE + IMAGE)
    // ==========================
    @PutMapping
    public User updateProfile(
            Authentication authentication,
            @RequestBody Map<String, String> payload
    ) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (payload.containsKey("fullName")) {
            user.setFullName(payload.get("fullName"));
        }

        if (payload.containsKey("phone")) {
            user.setPhone(payload.get("phone"));
        }

        if (payload.containsKey("profileImageUrl")) {
            user.setProfileImageUrl(payload.get("profileImageUrl"));
        }

        return userRepository.save(user);
    }

    // ==========================
    // ✅ OPTIONAL: UPDATE ONLY IMAGE (KEEPING YOUR OLD API)
    // ==========================
    @PutMapping("/image")
    public User updateProfileImage(
            Authentication authentication,
            @RequestParam String imageUrl
    ) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setProfileImageUrl(imageUrl);

        return userRepository.save(user);
    }
}
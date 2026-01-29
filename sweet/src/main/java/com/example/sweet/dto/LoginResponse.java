package com.example.sweet.dto;

import com.example.sweet.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private boolean success; // ✅ new (status flag)
    private String message;   // ✅ new (readable response)
    private String token;     // ✅ existing (unchanged)
    private User user;        // ✅ existing (unchanged)

    // ✅ BACKWARD-COMPATIBLE CONSTRUCTOR (DOES NOT BREAK OLD FLOW)
    public LoginResponse(String token, User user) {
        this.success = true;
        this.message = "Login successful";
        this.token = token;
        this.user = user;
    }
}

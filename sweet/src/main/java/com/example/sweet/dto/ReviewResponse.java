package com.example.sweet.dto;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        int rating,
        String comment,
        String userName,
        String image,
        LocalDateTime createdAt
) {}

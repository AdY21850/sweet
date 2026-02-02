package com.example.sweet.dto;

public record SweetResponse(
        Long id,
        String name,
        double price,
        String imageUrl
) {}

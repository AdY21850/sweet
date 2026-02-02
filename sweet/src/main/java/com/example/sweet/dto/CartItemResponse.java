package com.example.sweet.dto;

public record CartItemResponse(
        Long id,
        int quantity,
        SweetResponse sweet
) {}

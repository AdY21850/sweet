package com.example.sweet.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AddSweetRequest {

    @NotBlank(message = "Sweet name cannot be empty")
    private String name;

    @NotBlank(message = "Category cannot be empty")
    private String category;

    @Positive(message = "Price must be greater than 0")
    private double price;

    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantity;

    @NotBlank(message = "Image URL cannot be empty")
    private String imageUrl;

    @Size(max = 1000, message = "Description can be max 1000 characters")
    private String description;
}

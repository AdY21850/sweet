package com.example.sweet.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // ✅ REQUIRED BY SweetService
    private String category;

    private double price;

    private int quantity;

    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;
}
package com.example.sweet.repository;

import com.example.sweet.model.HeroSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeroSectionRepository extends JpaRepository<HeroSection, Long> {

    // ✅ Get currently active hero banner
    Optional<HeroSection> findByActiveTrue();
}

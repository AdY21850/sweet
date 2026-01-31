package com.example.sweet.repository;

import com.example.sweet.model.HeroSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeroSectionRepository extends JpaRepository<HeroSection, Long> {

    // ✅ Get ALL active hero banners
    List<HeroSection> findByActiveTrue();
}

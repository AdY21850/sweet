package com.example.sweet.controller;

import com.example.sweet.model.HeroSection;
import com.example.sweet.service.HeroSectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hero")
public class HeroSectionController {

    private final HeroSectionService service;

    public HeroSectionController(HeroSectionService service) {
        this.service = service;
    }

    @GetMapping("/active")
    public HeroSection getHero() {
        return service.getActiveHero();
    }
}

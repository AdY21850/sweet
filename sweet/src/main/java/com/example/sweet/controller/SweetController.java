package com.example.sweet.controller;

import com.example.sweet.model.Sweet;
import com.example.sweet.service.SweetService;
import com.example.sweet.service.PopularSweetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sweets")
public class SweetController {

    private final SweetService sweetService;
    private final PopularSweetService popularSweetService;

    public SweetController(
            SweetService sweetService,
            PopularSweetService popularSweetService
    ) {
        this.sweetService = sweetService;
        this.popularSweetService = popularSweetService;
    }

    // =====================================================
    // ✅ ADMIN — GET ALL SWEETS (THIS WAS MISSING)
    // =====================================================
    @GetMapping
    public List<Sweet> getAllSweets() {
        return sweetService.getAllSweets();
    }

    // =====================================================
    // USER — POPULAR SWEETS
    // =====================================================
    @GetMapping("/popular")
    public List<Sweet> getPopularSweets(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return popularSweetService.getPopularSweets(limit);
    }

    // =====================================================
    // USER — SWEETS BY CATEGORY
    // =====================================================
    @GetMapping("/by-category")
    public List<Sweet> getSweetsByCategory(
            @RequestParam String category
    ) {
        return sweetService.getSweetsByCategory(category);
    }
}

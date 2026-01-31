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

    // ================= EXISTING ENDPOINTS (UNCHANGED) =================
    // (Your existing methods stay exactly as they were)

    // ================= NEW ENDPOINT =================

    // ✅ Popular Sweets for Home Screen
    @GetMapping("/popular")
    public List<Sweet> getPopularSweets(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return popularSweetService.getPopularSweets(limit);
    }
}

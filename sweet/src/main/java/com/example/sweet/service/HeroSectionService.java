package com.example.sweet.service;

import com.example.sweet.exception.ResourceNotFoundException;
import com.example.sweet.model.HeroSection;
import com.example.sweet.repository.HeroSectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeroSectionService {

    private final HeroSectionRepository heroRepository;

    public HeroSectionService(HeroSectionRepository heroRepository) {
        this.heroRepository = heroRepository;
    }

    // ✅ PUBLIC — Get active hero banner (Home slider)
    public HeroSection getActiveHero() {
        return heroRepository.findByActiveTrue()
                .orElseThrow(() ->
                        new ResourceNotFoundException("No active hero banner found")
                );
    }

    // ✅ ADMIN — Get all hero banners
    public List<HeroSection> getAllHeroes() {
        return heroRepository.findAll();
    }

    // ✅ ADMIN — Add new hero banner
    public HeroSection addHero(HeroSection hero) {

        // If new hero is active, disable others
        if (hero.isActive()) {
            deactivateAllHeroes();
        }

        return heroRepository.save(hero);
    }

    // ✅ ADMIN — Update hero banner
    public HeroSection updateHero(Long id, HeroSection updatedHero) {

        HeroSection hero = heroRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hero banner not found with id " + id)
                );

        hero.setTitle(updatedHero.getTitle());
        hero.setSubtitle(updatedHero.getSubtitle());
        hero.setImageUrl(updatedHero.getImageUrl());
        hero.setActive(updatedHero.isActive());

        // If setting active, disable others
        if (updatedHero.isActive()) {
            deactivateAllHeroes();
        }

        return heroRepository.save(hero);
    }

    // ✅ ADMIN — Delete hero banner
    public void deleteHero(Long id) {
        if (!heroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hero banner not found with id " + id);
        }

        heroRepository.deleteById(id);
    }

    // ==========================
    // INTERNAL HELPER
    // ==========================
    private void deactivateAllHeroes() {
        heroRepository.findAll().forEach(hero -> {
            hero.setActive(false);
            heroRepository.save(hero);
        });
    }
}

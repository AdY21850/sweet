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

    // ✅ PUBLIC — Get ALL active heroes
    public List<HeroSection> getActiveHeroes() {
        return heroRepository.findByActiveTrue();
    }

    // ✅ ADMIN — Get all heroes
    public List<HeroSection> getAllHeroes() {
        return heroRepository.findAll();
    }

    // ✅ ADMIN — Add hero (NO auto-deactivate)
    public HeroSection addHero(HeroSection hero) {
        return heroRepository.save(hero);
    }

    // ✅ ADMIN — Update hero
    public HeroSection updateHero(Long id, HeroSection updatedHero) {

        HeroSection hero = heroRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hero banner not found with id " + id)
                );

        hero.setTitle(updatedHero.getTitle());
        hero.setSubtitle(updatedHero.getSubtitle());
        hero.setImageUrl(updatedHero.getImageUrl());
        hero.setActive(updatedHero.isActive());

        return heroRepository.save(hero);
    }

    // ✅ ADMIN — Delete hero
    public void deleteHero(Long id) {
        if (!heroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hero banner not found with id " + id);
        }
        heroRepository.deleteById(id);
    }
}

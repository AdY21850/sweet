package com.example.sweet.service;

import com.example.sweet.dto.AddSweetRequest;
import com.example.sweet.exception.ResourceNotFoundException;
import com.example.sweet.model.Sweet;
import com.example.sweet.repository.SweetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SweetService {

    private final SweetRepository sweetRepository;

    public SweetService(SweetRepository sweetRepository) {
        this.sweetRepository = sweetRepository;
    }

    public List<Sweet> getAllSweets() {
        return sweetRepository.findAll();
    }

    public Sweet addSweet(AddSweetRequest request) {

        Sweet sweet = new Sweet();
        sweet.setName(request.getName());
        sweet.setCategory(request.getCategory());
        sweet.setPrice(request.getPrice());
        sweet.setQuantity(request.getQuantity());
        sweet.setImageUrl(request.getImageUrl());
        sweet.setDescription(request.getDescription());

        return sweetRepository.save(sweet);
    }

    public List<Sweet> getSweetsByCategory(String category) {
        return sweetRepository.findByCategoryIgnoreCase(category);
    }


    public Sweet purchaseSweet(Long sweetId) {

        Sweet sweet = sweetRepository.findById(sweetId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sweet not found with id " + sweetId)
                );

        if (sweet.getQuantity() <= 0) {
            throw new IllegalStateException("Sweet out of stock");
        }

        sweet.setQuantity(sweet.getQuantity() - 1);
        return sweetRepository.save(sweet);
    }

    /**
     * Unified search method
     */
    public List<Sweet> search(
            String name,
            String category,
            Double minPrice,
            Double maxPrice
    ) {

        if (name != null) {
            return sweetRepository.findByNameContainingIgnoreCase(name);
        }

        if (category != null) {
            return sweetRepository.findByCategoryIgnoreCase(category);
        }

        if (minPrice != null && maxPrice != null) {
            return sweetRepository.findByPriceBetween(minPrice, maxPrice);
        }

        return sweetRepository.findAll();
    }
    public Sweet updateSweet(Long id, AddSweetRequest request) {

        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sweet not found with id " + id)
                );

        sweet.setName(request.getName());
        sweet.setCategory(request.getCategory());
        sweet.setPrice(request.getPrice());
        sweet.setQuantity(request.getQuantity());
        sweet.setImageUrl(request.getImageUrl());
        sweet.setDescription(request.getDescription());

        return sweetRepository.save(sweet);
    }
    public void deleteSweet(Long id) {
        if (!sweetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sweet not found with id " + id);
        }
        sweetRepository.deleteById(id);
    }

}

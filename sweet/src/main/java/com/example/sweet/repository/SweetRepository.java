package com.example.sweet.repository;

import com.example.sweet.model.Sweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SweetRepository extends JpaRepository<Sweet, Long> {

    List<Sweet> findByNameContainingIgnoreCase(String name);

    List<Sweet> findByCategoryIgnoreCase(String category);

    List<Sweet> findByPriceBetween(Double min, Double max);

    List<Sweet> findByIdInAndActiveTrue(List<Long> ids);
    List<Sweet> findByIdIn(List<Long> ids);

    // ✅ Optional safe helper
    boolean existsById(Long id);
}

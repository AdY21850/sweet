package com.example.sweet.repository;

import com.example.sweet.model.Review;
import com.example.sweet.model.Sweet;
import com.example.sweet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // ✅ Get all reviews for a sweet
    List<Review> findBySweetOrderByCreatedAtDesc(Sweet sweet);

    // ✅ Get review by user + sweet (enforce one review rule)
    Optional<Review> findByUserAndSweet(User user, Sweet sweet);

    // ✅ Get average rating (optional helper)
    List<Review> findBySweet(Sweet sweet);
}

package com.example.sweet.controller;

import com.example.sweet.model.Review;
import com.example.sweet.service.ReviewService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // ==========================
    // USER: ADD OR UPDATE REVIEW
    // ==========================
    @PostMapping("/{sweetId}")
    @PreAuthorize("hasRole('USER')")
    public Review addOrUpdateReview(
            Authentication authentication,
            @PathVariable Long sweetId,
            @RequestParam int rating,
            @RequestParam(required = false) String comment
    ) {
        String email = authentication.getName();
        return reviewService.addOrUpdateReview(email, sweetId, rating, comment);
    }

    // ==========================
    // PUBLIC: GET REVIEWS FOR SWEET
    // ==========================
    @GetMapping("/{sweetId}")
    public List<Review> getReviews(@PathVariable Long sweetId) {
        return reviewService.getReviewsForSweet(sweetId);
    }

    // ==========================
    // ADMIN: DELETE REVIEW
    // ==========================
    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}

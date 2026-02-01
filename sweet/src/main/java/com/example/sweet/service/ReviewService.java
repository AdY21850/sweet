package com.example.sweet.service;

import com.example.sweet.dto.ReviewResponse;
import com.example.sweet.exception.ResourceNotFoundException;
import com.example.sweet.model.Review;
import com.example.sweet.model.Sweet;
import com.example.sweet.model.User;
import com.example.sweet.repository.ReviewRepository;
import com.example.sweet.repository.SweetRepository;
import com.example.sweet.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final SweetRepository sweetRepository;
    private final UserRepository userRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            SweetRepository sweetRepository,
            UserRepository userRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.sweetRepository = sweetRepository;
        this.userRepository = userRepository;
    }

    // ==========================
    // ADD OR UPDATE REVIEW
    // ==========================
    public Review addOrUpdateReview(
            String email,
            Long sweetId,
            int rating,
            String comment
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Sweet sweet = sweetRepository.findById(sweetId)
                .orElseThrow(() -> new ResourceNotFoundException("Sweet not found"));

        Review review = reviewRepository.findByUserAndSweet(user, sweet)
                .orElse(new Review());

        review.setUser(user);
        review.setSweet(sweet);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    // ==========================
    // GET REVIEWS FOR SWEET (DTO)
    // ==========================
    public List<ReviewResponse> getReviewsForSweet(Long sweetId) {

        Sweet sweet = sweetRepository.findById(sweetId)
                .orElseThrow(() -> new ResourceNotFoundException("Sweet not found"));

        return reviewRepository.findBySweetOrderByCreatedAtDesc(sweet)
                .stream()
                .map(review -> new ReviewResponse(
                        review.getId(),
                        review.getRating(),
                        review.getComment(),
                        review.getUser() != null
                                ? review.getUser().getFullName()
                                : "Anonymous",
                        review.getCreatedAt()
                ))
                .toList();
    }

    // ==========================
    // DELETE REVIEW (ADMIN)
    // ==========================
    public void deleteReview(Long reviewId) {

        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found");
        }

        reviewRepository.deleteById(reviewId);
    }
}

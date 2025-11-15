package com.example.assignment.service;

import com.example.assignment.model.Review;
import com.example.assignment.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getReviewsByArticleId(Long articleId) {
        return reviewRepository.findByArticleId(articleId);
    }

    // Lấy review theo ID
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    // Thêm hoặc cập nhật review
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    // Xóa review theo ID
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}

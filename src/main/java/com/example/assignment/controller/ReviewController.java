package com.example.assignment.controller;

import com.example.assignment.model.Review;
import com.example.assignment.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // 📌 Form tạo mới (cần articleId để liên kết)
    @GetMapping("/new/{articleId}")
    public String createForm(@PathVariable Long articleId, Model model) {
        Review review = new Review();
        review.setArticleId(articleId);
        model.addAttribute("review", review);
        return "reviews/form";
    }

    // 📌 Lưu (tạo/cập nhật)
    @PostMapping
    public String save(@ModelAttribute Review review) {
        reviewService.saveReview(review);
        return "redirect:/articles/" + review.getArticleId();
    }

    // 📌 Form sửa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Optional<Review> review = reviewService.getReviewById(id);
        review.ifPresent(r -> model.addAttribute("review", r));
        return "reviews/form";
    }

    // 📌 Xóa
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Optional<Review> review = reviewService.getReviewById(id);
        if (review.isPresent()) {
            Long articleId = review.get().getArticleId();
            reviewService.deleteReview(id);
            return "redirect:/articles/" + articleId;
        }
        return "redirect:/articles";
    }
}

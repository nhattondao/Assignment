package com.example.assignment.controller;

import com.example.assignment.model.Review;
import com.example.assignment.repository.ReviewRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewRepository reviewRepo;

    public ReviewController(ReviewRepository reviewRepo){
        this.reviewRepo = reviewRepo;
    }

    // Form tạo mới (cần articleId để liên kết)
    @GetMapping("/new/{articleId}")
    public String createForm(@PathVariable Long articleId, Model model) {
        Review r = new Review();
        r.setArticleId(articleId);
        model.addAttribute("review", r);
        return "reviews/form";
    }

    // Lưu (tạo/cập nhật)
    @PostMapping
    public String save(@ModelAttribute Review review) {
        reviewRepo.save(review);
        return "redirect:/articles/" + review.getArticleId();
    }

    // Form sửa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        reviewRepo.findById(id).ifPresent(r -> model.addAttribute("review", r));
        return "reviews/form";
    }

    // Xóa
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        return reviewRepo.findById(id).map(r -> {
            Long aid = r.getArticleId();
            reviewRepo.deleteById(id);
            return "redirect:/articles/" + aid;
        }).orElse("redirect:/articles");
    }
}

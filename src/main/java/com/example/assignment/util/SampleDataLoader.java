package com.example.assignment.util;

import com.example.assignment.model.Article;
import com.example.assignment.model.Review;
import com.example.assignment.repository.ArticleRepository;
import com.example.assignment.repository.ReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SampleDataLoader implements CommandLineRunner {
    private final ArticleRepository articleRepo;
    private final ReviewRepository reviewRepo;

    public SampleDataLoader(ArticleRepository articleRepo, ReviewRepository reviewRepo) {
        this.articleRepo = articleRepo;
        this.reviewRepo = reviewRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        // Tạo bài viết mẫu
        Article a1 = new Article(null, "Kiến thức Java", "Java", "Nguyen A", "Java cơ bản tới Java nâng cao");
        a1.setCreatedAt(LocalDate.of(2024, 5, 10)); // Ngày đăng giả định

        Article a2 = new Article(null, "Javascript and HTML,CSS", "JavaScript", "John B", "Nâng cao về js và kết hợp html,css");
        a2.setCreatedAt(LocalDate.of(2025, 1, 20)); // Ngày đăng giả định

        articleRepo.save(a1);
        articleRepo.save(a2);

        // Gắn review vào các bài viết có sẵn
        reviewRepo.save(new Review(null, "Mastery", 5, "Khóa rất hay, nhiều bài tập thực tế", a1.getId()));
        reviewRepo.save(new Review(null, "Fast", 4, "Giải thích rõ ràng", a2.getId()));
    }
}

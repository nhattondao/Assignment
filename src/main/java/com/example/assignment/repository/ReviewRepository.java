package com.example.assignment.repository;

import com.example.assignment.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 👉 Thêm dòng này
    List<Review> findByArticleId(Long articleId);
}

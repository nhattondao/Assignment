package com.example.assignment.service;

import com.example.assignment.model.Article;
import com.example.assignment.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    public Article findById(Long id) {
        return articleRepository.findById(id).orElse(null);
    }




    public List<Article> searchArticles(String keyword, String language, LocalDate fromDate, LocalDate toDate) {
        LocalDateTime start = (fromDate != null) ? fromDate.atStartOfDay() : null;
        LocalDateTime end = (toDate != null) ? toDate.atTime(LocalTime.MAX) : null;
        return articleRepository.searchArticles(keyword, language, start, end);
    }




    // Lưu Article + ảnh từ MultipartFile
    public Article saveArticleWithImage(Article article, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            article.setImage(imageFile.getBytes());
        } else if (article.getId() != null) {
            Article existing = articleRepository.findById(article.getId()).orElse(null);
            if (existing != null && existing.getImage() != null) {
                article.setImage(existing.getImage());
            }
        }
        return articleRepository.save(article);
    }





    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}

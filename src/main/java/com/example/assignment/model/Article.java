package com.example.assignment.model;

import java.time.LocalDate;

public class Article {
    private Long id;
    private String title;
    private String language;
    private String author;
    private String content;

    // 📌 Ngày đăng (chỉ ngày)
    private LocalDate createdAt;

    public Article() {
        this.createdAt = LocalDate.now(); // mặc định là ngày hiện tại
    }

    public Article(Long id, String title, String language, String author, String content) {
        this.id = id;
        this.title = title;
        this.language = language;
        this.author = author;
        this.content = content;
        this.createdAt = LocalDate.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}

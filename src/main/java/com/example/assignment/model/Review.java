package com.example.assignment.model;

public class Review {
    private Long id;
    private String courseName;
    private int rating; // 1..5
    private String comment;
    private Long articleId; // liên kết tới Article


    public Review() {}
    public Review(Long id, String courseName, int rating, String comment, Long articleId) {
        this.id = id; this.courseName = courseName; this.rating = rating; this.comment = comment; this.articleId = articleId;
    }
    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }
}

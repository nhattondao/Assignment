package com.example.assignment.repository;

import com.example.assignment.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("""
    SELECT a FROM Article a
    WHERE 
      (:keyword IS NULL OR :keyword = '' OR LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
      AND (:language IS NULL OR :language = '' OR a.language = :language)
      AND ((:fromDate IS NULL) OR a.createdAt >= :fromDate)
      AND ((:toDate IS NULL) OR a.createdAt <= :toDate)
    ORDER BY a.createdAt DESC
""")
    List<Article> searchArticles(
            @Param("keyword") String keyword,
            @Param("language") String language,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);


}

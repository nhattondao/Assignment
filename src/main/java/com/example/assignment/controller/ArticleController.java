package com.example.assignment.controller;

import com.example.assignment.model.Article;
import com.example.assignment.model.Review;
import com.example.assignment.service.ArticleService;
import com.example.assignment.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final ReviewService reviewService;

    public ArticleController(ArticleService articleService, ReviewService reviewService) {
        this.articleService = articleService;
        this.reviewService = reviewService;
    }

    // ✅ Danh sách bài viết + bộ lọc (gộp)
    @GetMapping
    public String listArticles(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Model model) {

        boolean hasFilter = (keyword != null && !keyword.isEmpty())
                || (language != null && !language.isEmpty())
                || fromDate != null || toDate != null;

        List<Article> articles = hasFilter
                ? articleService.searchArticles(keyword, language, fromDate, toDate)
                : articleService.getAllArticles();

        model.addAttribute("articles", articles);
        model.addAttribute("keyword", keyword);
        model.addAttribute("language", language);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        return "articles/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("article", new Article());
        return "articles/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        articleService.getArticleById(id)
                .ifPresent(a -> model.addAttribute("article", a));
        return "articles/form";
    }

    @PostMapping
    public String save(@ModelAttribute Article article,
                       @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                       RedirectAttributes redirectAttrs) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                article.setImage(imageFile.getBytes());
            } else if (article.getId() != null) {
                Article existing = articleService.findById(article.getId());
                if (existing != null && existing.getImage() != null) {
                    article.setImage(existing.getImage());
                }
            }

            Article saved = articleService.saveArticle(article);
            redirectAttrs.addFlashAttribute("message", "Lưu thành công");
            return "redirect:/articles/" + saved.getId();

        } catch (IOException e) {
            redirectAttrs.addFlashAttribute("error", "Lỗi khi upload ảnh: " + e.getMessage());
            return "redirect:/articles";
        }
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        return articleService.getArticleById(id)
                .filter(a -> a.getImage() != null)
                .map(a -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE,
                                Optional.ofNullable(a.getImageType()).orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                        .body(a.getImage()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public String view(
            @PathVariable Long id,
            @RequestParam(value = "sort", required = false) String sort,  // 👈 thêm tham số sort
            Model model,
            RedirectAttributes redirectAttrs) {

        Optional<Article> article = articleService.getArticleById(id);
        if (article.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "Không tìm thấy bài viết");
            return "redirect:/articles";
        }

        model.addAttribute("article", article.get());

        // ✅ Lấy danh sách review
        List<Review> reviews = reviewService.getReviewsByArticleId(id);

        // ✅ Nếu có sort thì sắp xếp
        if (sort != null) {
            if (sort.equalsIgnoreCase("asc")) {
                reviews.sort(Comparator.comparingInt(Review::getRating));
            } else if (sort.equalsIgnoreCase("desc")) {
                reviews.sort(Comparator.comparingInt(Review::getRating).reversed());
            }
        }

        model.addAttribute("reviews", reviews);
        model.addAttribute("currentSort", sort); // để sau này hiển thị trạng thái nếu cần

        return "articles/view";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return "redirect:/articles";
    }

    @GetMapping("/bookmark/{id}")
    public String toggleBookmark(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttrs) {
        Set<Long> bookmarks = (Set<Long>) session.getAttribute("bookmarks");
        if (bookmarks == null) bookmarks = new HashSet<>();

        String message;
        if (bookmarks.contains(id)) {
            bookmarks.remove(id);
            message = "❌ Đã bỏ khỏi Bookmark";
        } else {
            bookmarks.add(id);
            message = "✅ Đã thêm vào Bookmark";
        }

        session.setAttribute("bookmarks", bookmarks);
        redirectAttrs.addFlashAttribute("message", message);
        return "redirect:/articles";
    }

    @GetMapping("/bookmarks")
    public String bookmarksList(HttpSession session, Model model) {
        Set<Long> bookmarks = (Set<Long>) session.getAttribute("bookmarks");
        if (bookmarks == null) bookmarks = new HashSet<>();

        Set<Long> finalBookmarks = bookmarks;
        List<Article> bookmarkedArticles = articleService.getAllArticles().stream()
                .filter(a -> finalBookmarks.contains(a.getId()))
                .toList();

        model.addAttribute("bookmarkedArticles", bookmarkedArticles);
        return "bookmarks/list";
    }
}

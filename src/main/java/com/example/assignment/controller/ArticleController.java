package com.example.assignment.controller;

import com.example.assignment.model.Article;
import com.example.assignment.repository.ArticleRepository;
import com.example.assignment.repository.ReviewRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleRepository articleRepo;
    private final ReviewRepository reviewRepo;

    public ArticleController(ArticleRepository articleRepo, ReviewRepository reviewRepo) {
        this.articleRepo = articleRepo;
        this.reviewRepo = reviewRepo;
    }

    // 📌 Danh sách + tìm kiếm
    @GetMapping
    public String list(@RequestParam(required = false) String q,
                       @RequestParam(required = false) String language,
                       Model model) {
        List<Article> articles = articleRepo.search(q, language);
        model.addAttribute("articles", articles);
        model.addAttribute("q", q);
        model.addAttribute("language", language);
        return "articles/list";
    }

    // 📌 Form tạo mới
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("article", new Article());
        return "articles/form";
    }

    // 📌 Form sửa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        articleRepo.findById(id).ifPresent(a -> model.addAttribute("article", a));
        return "articles/form";
    }

    // 📌 Lưu (tạo mới hoặc cập nhật)
    @PostMapping
    public String save(@ModelAttribute Article article) {
        articleRepo.save(article);
        return article.getId() != null
                ? "redirect:/articles/" + article.getId()
                : "redirect:/articles";
    }

    // 📌 Xem chi tiết bài viết + review
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model, RedirectAttributes redirectAttrs) {
        return articleRepo.findById(id)
                .map(article -> {
                    model.addAttribute("article", article);
                    model.addAttribute("reviews", reviewRepo.findByArticleId(article.getId()));
                    return "articles/view";
                })
                .orElseGet(() -> {
                    redirectAttrs.addFlashAttribute("error", "❌ Không tìm thấy bài viết có ID = " + id);
                    return "redirect:/articles";
                });
    }


    // 📌 Xóa
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        articleRepo.deleteById(id);
        return "redirect:/articles";
    }

    // 📌 Toggle Bookmark (thêm hoặc bỏ)
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


    // 📌 Danh sách bookmark
    @GetMapping("/bookmarks")
    public String bookmarksList(HttpSession session, Model model) {
        Set<Long> bookmarks = (Set<Long>) session.getAttribute("bookmarks");
        if (bookmarks == null) bookmarks = new HashSet<>();

        // Lọc bài viết có trong danh sách bookmark
        Set<Long> finalBookmarks = bookmarks;
        List<Article> bookmarkedArticles = articleRepo.findAll().stream()
                .filter(a -> finalBookmarks.contains(a.getId()))
                .toList();

        model.addAttribute("bookmarkedArticles", bookmarkedArticles);
        return "bookmarks/list";  // ✅ Đúng với file bạn đã tạo trong /templates/bookmarks/list.html
    }
}

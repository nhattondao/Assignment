package com.example.assignment.repository;


import com.example.assignment.model.Article;
import org.springframework.stereotype.Repository;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Repository
public class ArticleRepository {
    private Map<Long, Article> store = new ConcurrentHashMap<>();
    private AtomicLong seq = new AtomicLong(1);


    public Article save(Article a) {
        if (a.getId() == null) a.setId(seq.getAndIncrement());
        store.put(a.getId(), a);
        return a;
    }


    public Optional<Article> findById(Long id) { return Optional.ofNullable(store.get(id)); }
    public List<Article> findAll() { return new ArrayList<>(store.values()); }
    public void deleteById(Long id) { store.remove(id); }


    public List<Article> search(String q, String language) {
        return store.values().stream()
                .filter(a -> (q==null || q.isBlank() || a.getTitle().toLowerCase().contains(q.toLowerCase()) || a.getContent().toLowerCase().contains(q.toLowerCase())))
                .filter(a -> (language==null || language.isBlank() || a.getLanguage().equalsIgnoreCase(language)))
                .sorted(Comparator.comparing(Article::getId).reversed())
                .collect(Collectors.toList());
    }
}

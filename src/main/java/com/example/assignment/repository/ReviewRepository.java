package com.example.assignment.repository;

import com.example.assignment.model.Review;
import org.springframework.stereotype.Repository;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Repository
public class ReviewRepository {
    private Map<Long, Review> store = new ConcurrentHashMap<>();
    private AtomicLong seq = new AtomicLong(1);


    public Review save(Review r) {
        if (r.getId() == null) r.setId(seq.getAndIncrement());
        store.put(r.getId(), r);
        return r;
    }


    public Optional<Review> findById(Long id) { return Optional.ofNullable(store.get(id)); }
    public List<Review> findAll() { return new ArrayList<>(store.values()); }
    public void deleteById(Long id) { store.remove(id); }
    public List<Review> findByArticleId(Long articleId) {
        return store.values().stream().filter(r -> Objects.equals(r.getArticleId(), articleId)).collect(Collectors.toList());
    }
}

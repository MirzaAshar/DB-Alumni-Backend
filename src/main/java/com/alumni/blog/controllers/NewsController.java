package com.alumni.blog.controllers;

import com.alumni.blog.entities.News;
import com.alumni.blog.entities.User;
import com.alumni.blog.payloads.ApiResponse;
import com.alumni.blog.repository.NewsRepo;
import com.alumni.blog.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsRepo newsRepo;

    @Autowired
    private UserRepo userRepo;

    // Create News with optional image
    @PostMapping(value = "/create/{authorId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<News> createNews(
            @PathVariable Integer authorId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        User author = userRepo.findById(authorId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + authorId));

        News news = new News();
        news.setTitle(title);
        news.setContent(content);
        news.setAuthor_name(author.getUsername());
        news.setAuthor_id(author.getId());
        news.setCreatedAt(LocalDateTime.now());

        if (image != null && !image.isEmpty()) {
            try {
                news.setImage(Base64.getEncoder().encodeToString(image.getBytes()).getBytes()); // Convert image to Base64
            } catch (IOException e) {
                throw new RuntimeException("Error processing image file: " + e.getMessage());
            }
        }

        News savedNews = newsRepo.save(news);
        return ResponseEntity.ok(savedNews);
    }
    // Get All News
    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {
        List<News> newsList = newsRepo.findAll();
        return ResponseEntity.ok(newsList);
    }

    // Get News by ID
    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        News news = newsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + id));
        return ResponseEntity.ok(news);
    }

    // Update News with optional image
    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(
            @PathVariable Long id,
            @RequestBody News updatedNews,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        News existingNews = newsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + id));

        existingNews.setTitle(updatedNews.getTitle());
        existingNews.setContent(updatedNews.getContent());
        existingNews.setUpdatedAt(LocalDateTime.now());

        if (image != null && !image.isEmpty()) {
            try {
                existingNews.setImage(image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error processing image file", e);
            }
        }

        News savedNews = newsRepo.save(existingNews);
        return ResponseEntity.ok(savedNews);
    }

    // Delete News
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteNews(@PathVariable Long id) {
        News news = newsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + id));

        newsRepo.delete(news);
        return ResponseEntity.ok(new ApiResponse("News deleted successfully", true));
    }
}

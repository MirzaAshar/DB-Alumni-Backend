package com.alumni.blog.controllers;

import com.alumni.blog.entities.Log;
import com.alumni.blog.entities.News;
import com.alumni.blog.entities.User;
import com.alumni.blog.payloads.ApiResponse;
import com.alumni.blog.payloads.PostDto;
import com.alumni.blog.repository.LogRepository;
import com.alumni.blog.repository.NewsRepo;
import com.alumni.blog.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    @Autowired
    private LogRepository logRepository;

    // Create News with optional image
    @PostMapping("/create/{authorId}")
    public ResponseEntity<News> createNews(
            @RequestPart("news") News news,
            @RequestPart(value = "image", required = false) MultipartFile image, @PathVariable int authorId) {

        User author = userRepo.findById(authorId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + authorId));

        News newss = new News();
        newss.setTitle(news.getTitle());
        newss.setContent(news.getContent());
        newss.setAuthor_name(author.getUsername());
        newss.setAuthor_id(author.getId());
        newss.setCreatedAt(LocalDateTime.now());
        String imageDirectory = "E:\\Blog-Api-Final\\images-news";
        if (image != null && !image.isEmpty()) {
            // Generate a unique image name
            String imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            String imagePath = imageDirectory + File.separator + imageName;

            // Ensure the directory exists
            File directory = new File(imageDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Save the file to the directory
            try {
                // Save the file
                Files.copy(image.getInputStream(), Paths.get(imagePath));
                news.setImageName(imageName);
                news.setImageData(image.getBytes());
            } catch (IOException ex) {
                throw new RuntimeException("Failed to save the image file: " + ex.getMessage(), ex);
            }
            news.setImageName(imageName);
        } else {
            news.setImageName("default.png"); // Use a default image if no file is uploaded
        }

        News savedNews = newsRepo.save(news);
        Log log = new Log();
        log.setActionType("CREATE");
        log.setUserId((long) author.getId());
        log.setLogMessage("News created by user with: " +author.getId());
        return ResponseEntity.ok(savedNews);
    }
    // Get All News
    @GetMapping("/")
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
            @RequestBody News updatedNews) {
        News existingNews = newsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + id));

        existingNews.setTitle(updatedNews.getTitle());
        existingNews.setContent(updatedNews.getContent());
        existingNews.setUpdatedAt(LocalDateTime.now());

        News savedNews = newsRepo.save(existingNews);
        Log log = new Log();
        log.setActionType("UPDATE");

        log.setLogMessage("News updated with news ID"+ existingNews.getId());
        return ResponseEntity.ok(savedNews);
    }

    // Delete News
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteNews(@PathVariable Long id) {
        News news = newsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + id));

        newsRepo.delete(news);
        Log log = new Log();
        log.setActionType("DELETE");
        log.setUserId(id.longValue());
        log.setLogMessage("News deleted with ID: " + id);
        return ResponseEntity.ok(new ApiResponse("News deleted successfully", true));
    }
}

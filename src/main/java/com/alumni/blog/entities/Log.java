package com.alumni.blog.entities;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@Data
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "action_type", nullable = false)
    private String actionType;

    @Column(name = "user_id", nullable = true)
    private Long userId;

    @Column(name = "log_message", columnDefinition = "TEXT")
    private String logMessage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Log() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
}

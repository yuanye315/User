package com.example.backend.dto;

import java.time.LocalDateTime;

public class UserDto {
    private Long id;
    private String userName;
    private LocalDateTime createdAt;

    // Constructor
    public UserDto(Long id, String userName, LocalDateTime createdAt) {
        this.id = id;
        this.userName = userName;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

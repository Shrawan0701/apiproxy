package com.apilimiter.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quotas")
public class Quota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer count;

    @Column(name = "reset_time", nullable = false)
    private LocalDateTime resetTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Quota() {}

    public Quota(User user, Integer count, LocalDateTime resetTime) {
        this.user = user;
        this.count = count;
        this.resetTime = resetTime;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }

    public LocalDateTime getResetTime() { return resetTime; }
    public void setResetTime(LocalDateTime resetTime) { this.resetTime = resetTime; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
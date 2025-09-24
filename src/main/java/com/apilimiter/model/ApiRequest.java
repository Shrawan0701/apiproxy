package com.apilimiter.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_requests")
public class ApiRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String endpoint;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Long latency;

    @Column(nullable = false)
    private Boolean success;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    public ApiRequest() {}

    public ApiRequest(User user, String endpoint, Long latency, Boolean success, Integer statusCode, String errorMessage) {
        this.user = user;
        this.endpoint = endpoint;
        this.timestamp = LocalDateTime.now();
        this.latency = latency;
        this.success = success;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Long getLatency() { return latency; }
    public void setLatency(Long latency) { this.latency = latency; }

    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }

    public Integer getStatusCode() { return statusCode; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
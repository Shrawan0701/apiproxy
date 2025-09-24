package com.apilimiter.service;

import com.apilimiter.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimitService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final int DAILY_QUOTA = 1000;

    public boolean isWithinLimit(User user) {
        String key = getDailyKey(user);
        Integer currentCount = (Integer) redisTemplate.opsForValue().get(key);
        return currentCount == null || currentCount < DAILY_QUOTA;
    }

    public int incrementAndGetCount(User user) {
        String key = getDailyKey(user);

        Long newCount = redisTemplate.opsForValue().increment(key, 1);

        // Set expiry to end of day if this is the first request
        if (newCount == 1) {
            LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT);
            LocalDateTime now = LocalDateTime.now();
            long secondsUntilEndOfDay = java.time.Duration.between(now, endOfDay).getSeconds();
            redisTemplate.expire(key, secondsUntilEndOfDay, TimeUnit.SECONDS);
        }

        return newCount.intValue();
    }

    public int getRemainingQuota(User user) {
        String key = getDailyKey(user);
        Integer currentCount = (Integer) redisTemplate.opsForValue().get(key);
        return DAILY_QUOTA - (currentCount != null ? currentCount : 0);
    }

    public int getTodayCount(User user) {
        String key = getDailyKey(user);
        Integer currentCount = (Integer) redisTemplate.opsForValue().get(key);
        return currentCount != null ? currentCount : 0;
    }

    public int getQuotaLimit() {
        return DAILY_QUOTA;
    }

    private String getDailyKey(User user) {
        return "quota:" + user.getId() + ":" + LocalDate.now().toString();
    }
}
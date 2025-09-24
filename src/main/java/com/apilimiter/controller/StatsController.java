package com.apilimiter.controller;

import com.apilimiter.dto.StatsDto;
import com.apilimiter.model.User;
import com.apilimiter.service.StatsService;
import com.apilimiter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StatsController {

    @Autowired
    private StatsService statsService;

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity<?> getUserStats(Authentication authentication) {
        Optional<User> userOpt = userService.findByEmail(authentication.getName());

        if (userOpt.isPresent()) {
            StatsDto.UserStats stats = statsService.getUserStats(userOpt.get());
            return ResponseEntity.ok(stats);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/today")
    public ResponseEntity<?> getTodayStats(Authentication authentication) {
        Optional<User> userOpt = userService.findByEmail(authentication.getName());

        if (userOpt.isPresent()) {
            StatsDto.DailyStats todayStats = statsService.getTodayStats(userOpt.get());
            return ResponseEntity.ok(todayStats);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getHistory(
            @RequestParam(defaultValue = "7") int days,
            Authentication authentication) {

        Optional<User> userOpt = userService.findByEmail(authentication.getName());

        if (userOpt.isPresent()) {
            List<StatsDto.DailyStats> history = statsService.getDailyHistory(userOpt.get(), days);
            return ResponseEntity.ok(history);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
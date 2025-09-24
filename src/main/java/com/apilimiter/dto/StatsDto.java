package com.apilimiter.dto;

import java.time.LocalDateTime;
import java.util.List;

public class StatsDto {

    public static class DailyStats {
        private LocalDateTime date;
        private long requestCount;
        private double avgLatency;
        private double errorRate;

        public DailyStats() {}

        public DailyStats(LocalDateTime date, long requestCount, double avgLatency, double errorRate) {
            this.date = date;
            this.requestCount = requestCount;
            this.avgLatency = avgLatency;
            this.errorRate = errorRate;
        }

        public LocalDateTime getDate() { return date; }
        public void setDate(LocalDateTime date) { this.date = date; }

        public long getRequestCount() { return requestCount; }
        public void setRequestCount(long requestCount) { this.requestCount = requestCount; }

        public double getAvgLatency() { return avgLatency; }
        public void setAvgLatency(double avgLatency) { this.avgLatency = avgLatency; }

        public double getErrorRate() { return errorRate; }
        public void setErrorRate(double errorRate) { this.errorRate = errorRate; }
    }

    public static class UserStats {
        private long totalRequests;
        private int todayRequests;
        private int quotaLimit;
        private double avgLatency;
        private double errorRate;
        private List<DailyStats> history;

        public UserStats() {}

        public long getTotalRequests() { return totalRequests; }
        public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }

        public int getTodayRequests() { return todayRequests; }
        public void setTodayRequests(int todayRequests) { this.todayRequests = todayRequests; }

        public int getQuotaLimit() { return quotaLimit; }
        public void setQuotaLimit(int quotaLimit) { this.quotaLimit = quotaLimit; }

        public double getAvgLatency() { return avgLatency; }
        public void setAvgLatency(double avgLatency) { this.avgLatency = avgLatency; }

        public double getErrorRate() { return errorRate; }
        public void setErrorRate(double errorRate) { this.errorRate = errorRate; }

        public List<DailyStats> getHistory() { return history; }
        public void setHistory(List<DailyStats> history) { this.history = history; }
    }
}
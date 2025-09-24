package com.apilimiter.service;

import com.apilimiter.dto.StatsDto;
import com.apilimiter.model.User;
import com.apilimiter.repository.ApiRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatsService {

    @Autowired
    private ApiRequestRepository apiRequestRepository;

    @Autowired
    private RateLimitService rateLimitService;

    public StatsDto.UserStats getUserStats(User user) {
        StatsDto.UserStats stats = new StatsDto.UserStats();

        stats.setTotalRequests(apiRequestRepository.countByUser(user));
        stats.setTodayRequests(rateLimitService.getTodayCount(user));
        stats.setQuotaLimit(rateLimitService.getQuotaLimit());


        Number avgLatencyNum = apiRequestRepository.findAverageLatencyByUser(user);
        stats.setAvgLatency(avgLatencyNum != null ? avgLatencyNum.doubleValue() : 0.0);

        Number errorRateNum = apiRequestRepository.findErrorRateByUser(user);
        stats.setErrorRate(errorRateNum != null ? errorRateNum.doubleValue() : 0.0);

        stats.setHistory(getDailyHistory(user, 7));
        return stats;
    }

    public List<StatsDto.DailyStats> getDailyHistory(User user, int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        List<Object[]> rawData = apiRequestRepository.findDailyStatsBy(user, startDate);

        List<StatsDto.DailyStats> history = new ArrayList<>();

        for (Object[] row : rawData) {

            LocalDateTime date;
            Object rawDate = row[0];
            if (rawDate instanceof java.sql.Timestamp) {
                date = ((java.sql.Timestamp) rawDate).toLocalDateTime();
            } else if (rawDate instanceof java.sql.Date) {
                date = ((java.sql.Date) rawDate).toLocalDate().atStartOfDay();
            } else if (rawDate instanceof LocalDateTime) {
                date = (LocalDateTime) rawDate;
            } else {
                throw new IllegalStateException("Unexpected date type: " + rawDate.getClass());
            }


            long requestCount = ((Number) row[1]).longValue();
            double avgLatency = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;
            double errorRate = row[3] != null ? ((Number) row[3]).doubleValue() : 0.0;

            history.add(new StatsDto.DailyStats(date, requestCount, avgLatency, errorRate));
        }

        return history;
    }

    public StatsDto.DailyStats getTodayStats(User user) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        long requestCount = apiRequestRepository.countByUserAndDateRange(user, startOfDay, endOfDay);

        Number avgLatencyNum = apiRequestRepository.findAverageLatencyByUser(user);
        Number errorRateNum = apiRequestRepository.findErrorRateByUser(user);

        return new StatsDto.DailyStats(
                LocalDateTime.now(),
                requestCount,
                avgLatencyNum != null ? avgLatencyNum.doubleValue() : 0.0,
                errorRateNum != null ? errorRateNum.doubleValue() : 0.0
        );
    }
}

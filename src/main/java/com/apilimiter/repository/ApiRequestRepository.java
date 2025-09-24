package com.apilimiter.repository;

import com.apilimiter.model.ApiRequest;
import com.apilimiter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApiRequestRepository extends JpaRepository<ApiRequest, Long> {

    List<ApiRequest> findByUserAndTimestampBetween(User user, LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(ar) FROM ApiRequest ar WHERE ar.user = :user AND ar.timestamp >= :start AND ar.timestamp < :end")
    long countByUserAndDateRange(@Param("user") User user, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // ✅ Return Number instead of Double (avoids ClassCastException with Float/BigDecimal)
    @Query("SELECT AVG(ar.latency) FROM ApiRequest ar WHERE ar.user = :user")
    Number findAverageLatencyByUser(@Param("user") User user);

    @Query("SELECT CASE WHEN (SELECT COUNT(ar2) FROM ApiRequest ar2 WHERE ar2.user = :user) = 0 THEN 0.0 " +
            "ELSE COUNT(ar) * 100.0 / (SELECT COUNT(ar2) FROM ApiRequest ar2 WHERE ar2.user = :user) END " +
            "FROM ApiRequest ar WHERE ar.user = :user AND ar.success = false")
    Number findErrorRateByUser(@Param("user") User user);

    @Query("SELECT DATE(ar.timestamp), COUNT(ar), AVG(ar.latency), " +
            "COUNT(CASE WHEN ar.success = false THEN 1 END) * 100.0 / COUNT(ar) " +
            "FROM ApiRequest ar WHERE ar.user = :user AND ar.timestamp >= :start " +
            "GROUP BY DATE(ar.timestamp) ORDER BY DATE(ar.timestamp)")
    List<Object[]> findDailyStatsBy(@Param("user") User user, @Param("start") LocalDateTime start);

    long countByUser(User user);
}

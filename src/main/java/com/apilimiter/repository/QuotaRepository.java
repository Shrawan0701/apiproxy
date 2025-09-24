package com.apilimiter.repository;

import com.apilimiter.model.Quota;
import com.apilimiter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface QuotaRepository extends JpaRepository<Quota, Long> {
    Optional<Quota> findByUserAndResetTimeAfter(User user, LocalDateTime currentTime);
    void deleteByResetTimeBefore(LocalDateTime cutoffTime);
}
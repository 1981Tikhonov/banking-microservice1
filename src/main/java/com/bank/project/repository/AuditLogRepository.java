package com.bank.project.repository;

import com.bank.project.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    @Query("SELECT a.username, COUNT(a) as attemptCount FROM AuditLog a " +
           "WHERE a.action = 'LOGIN' AND a.status = 'FAILED' AND a.timestamp >= :since " +
           "GROUP BY a.username HAVING COUNT(a) >= :minAttempts")
    List<Object[]> findUsersWithMultipleFailedLogins(
        @Param("since") LocalDateTime since,
        @Param("minAttempts") int minAttempts
    );

    long countByUsernameAndActionAndStatusAndTimestampAfter(
        String username, String action, String status, LocalDateTime timestamp);
        
    long countByUsernameAndActionContainingAndTimestampAfter(
        String username, String action, LocalDateTime timestamp);
}

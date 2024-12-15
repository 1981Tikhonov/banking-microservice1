package com.bank.project.repository;

import com.bank.project.entity.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Long> {

    List<Agreement> findByAccountId(Long accountId);

    List<Agreement> findByProductId(Long productId);

    List<Agreement> findByStatus(String status);

    List<Agreement> findByInterestRateGreaterThan(Double interestRate);

    List<Agreement> findBySumLessThan(Double sum);

    List<Agreement> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Agreement> findByUpdatedAtAfter(LocalDateTime updatedAt);
}

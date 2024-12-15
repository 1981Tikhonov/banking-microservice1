package com.bank.project.repository;

import com.bank.project.entity.Account;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByName(String name);

    List<Account> findAllByStatus(String status);

    List<Account> findAllByBalanceLessThan(BigDecimal balance);

    List<Account> findAllByCurrencyCode(String currencyCode);

    List<Account> findAllByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Account> findAllByUpdatedAtAfter(LocalDateTime updatedAt);

    boolean existsById(@NotNull Long id);

    List<Account> findAllByClientId(Long clientId);
}

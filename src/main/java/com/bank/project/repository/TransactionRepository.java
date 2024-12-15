package com.bank.project.repository;

import com.bank.project.entity.Transaction;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @NotNull Optional<Transaction> findById(@NotNull Long id);

    List<Transaction> findByDebitAccountId(Long debitAccountId);

    List<Transaction> findByCreditAccountId(Long creditAccountId);

    List<Transaction> findByType(String type);

    List<Transaction> findByAmount(Double amount);

    List<Transaction> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}

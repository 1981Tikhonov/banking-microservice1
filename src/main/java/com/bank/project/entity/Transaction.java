package com.bank.project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "debit_account_id", nullable = false)
    private Long debitAccountId;

    @Column(name = "credit_account_id", nullable = false)
    private Long creditAccountId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Double amount;

    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Transaction(long l, long l1, long l2, String transfer, double v, LocalDateTime now, LocalDateTime now1) {

    }

    public Transaction() {

    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void setSourceAccount(Account sourceAccount) {
        this.debitAccountId = sourceAccount.getId();
    }

    public void setTargetAccount(Account targetAccount) {
        this.creditAccountId = targetAccount.getId();
    }

    public void setAmount(BigDecimal bigDecimal) {
        this.amount = bigDecimal.doubleValue();
    }
}

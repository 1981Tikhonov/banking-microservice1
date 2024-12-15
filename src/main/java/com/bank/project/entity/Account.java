package com.bank.project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)  // Обязательно указывать клиента
    private Client client;

    private String name;
    private String type;

    @Column(name = "status")
    private String status;


    @Column(precision = 15, scale = 2)  // Уточнение для BigDecimal (деньги с двумя знаками после запятой)
    private BigDecimal balance;

    @Column(name = "currency_code")
    private Integer currencyCode;


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Метод для инициализации времени при создании
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    // Метод для обновления времени при изменении сущности
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Throwable getClientId() {
            return null;
    }

    public void setClientId(long l) {

    }
}

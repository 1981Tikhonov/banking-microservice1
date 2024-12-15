package com.bank.project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "currency")
public class Currency {

    public enum CurrencyCode {
        USD, EUR, GBP


    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private String symbol;
    private Double exchangeRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Currency(long l, String usd, String usDollar, String usd1, double v) {

    }

    public Currency() {

    }
}

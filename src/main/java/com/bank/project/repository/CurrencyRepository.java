package com.bank.project.repository;

import com.bank.project.entity.Currency;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    // Найти валюту по коду
    Currency findByCode(String code);

    // Найти валюту по названию
    Currency findByName(String name);

    // Найти валюту по символу
    Currency findBySymbol(String symbol);

    // Найти валюту по обменному курсу
    Currency findByExchangeRate(Double exchangeRate);

    // Найти валюту по ID
    @NotNull Optional<Currency> findById(@NotNull Long id);
}

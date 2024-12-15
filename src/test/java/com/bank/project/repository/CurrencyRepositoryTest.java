package com.bank.project.repository;

import com.bank.project.entity.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    private Currency usdCurrency;

    @BeforeEach
    void setUp() {
        // Создаем примеры валют для тестирования
        usdCurrency = new Currency();
        usdCurrency.setCode("USD");
        usdCurrency.setName("US Dollar");
        usdCurrency.setSymbol("$");
        usdCurrency.setExchangeRate(1.0);

        Currency eurCurrency = new Currency();
        eurCurrency.setCode("EUR");
        eurCurrency.setName("Euro");
        eurCurrency.setSymbol("€");
        eurCurrency.setExchangeRate(0.9);

        // Сохраняем данные в базу для тестирования
        currencyRepository.save(usdCurrency);
        currencyRepository.save(eurCurrency);
    }

    @Test
    void testFindByCode() {
        // Проверка метода поиска валюты по коду
        Currency currency = currencyRepository.findByCode("USD");
        assertThat(currency).isNotNull();
        assertThat(currency.getCode()).isEqualTo("USD");
    }

    @Test
    void testFindByName() {
        // Проверка метода поиска валюты по названию
        Currency currency = currencyRepository.findByName("Euro");
        assertThat(currency).isNotNull();
        assertThat(currency.getName()).isEqualTo("Euro");
    }

    @Test
    void testFindBySymbol() {
        // Проверка метода поиска валюты по символу
        Currency currency = currencyRepository.findBySymbol("$");
        assertThat(currency).isNotNull();
        assertThat(currency.getSymbol()).isEqualTo("$");
    }

    @Test
    void testFindByExchangeRate() {
        // Проверка метода поиска валюты по обменному курсу
        Currency currency = currencyRepository.findByExchangeRate(0.9);
        assertThat(currency).isNotNull();
        assertThat(currency.getExchangeRate()).isEqualTo(0.9);
    }

    @Test
    void testFindById() {
        // Проверка метода поиска валюты по ID
        Currency currency = currencyRepository.findById(usdCurrency.getId()).orElse(null);
        assertThat(currency).isNotNull();
        assertThat(currency.getId()).isEqualTo(usdCurrency.getId());
    }
}

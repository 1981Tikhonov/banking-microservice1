package com.bank.project.service;

import com.bank.project.entity.Currency;
import com.bank.project.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyService currencyService;

    private Currency currency;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Создание mock валюты для тестов
        currency = new Currency();
        currency.setId(1L);
        currency.setCode("USD");
        currency.setName("US Dollar");
        currency.setSymbol("$");
        currency.setExchangeRate(1.0);
        currency.setCreatedAt(LocalDateTime.now());
        currency.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateCurrency() {
        when(currencyRepository.save(any(Currency.class))).thenReturn(currency);

        Currency createdCurrency = currencyService.createCurrency(currency);

        assertNotNull(createdCurrency);
        assertEquals(currency.getCode(), createdCurrency.getCode());
        verify(currencyRepository, times(1)).save(currency);
    }

    @Test
    void testGetCurrencyById() {
        when(currencyRepository.findById(1L)).thenReturn(Optional.of(currency));

        Currency foundCurrency = currencyService.getCurrencyById(1L);

        assertNotNull(foundCurrency);
        assertEquals(currency.getCode(), foundCurrency.getCode());
    }

    @Test
    void testGetCurrencyById_NotFound() {
        when(currencyRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> currencyService.getCurrencyById(1L));

        assertEquals("Currency not found with ID: 1", exception.getMessage());
    }

    @Test
    void testUpdateCurrency() {
        Currency updatedCurrencyData = new Currency();
        updatedCurrencyData.setCode("EUR");
        updatedCurrencyData.setName("Euro");
        updatedCurrencyData.setSymbol("€");
        updatedCurrencyData.setExchangeRate(0.85);

        when(currencyRepository.findById(1L)).thenReturn(Optional.of(currency));
        when(currencyRepository.save(any(Currency.class))).thenReturn(updatedCurrencyData);

        Currency updatedCurrency = currencyService.updateCurrency(1L, updatedCurrencyData);

        assertEquals("EUR", updatedCurrency.getCode());
        assertEquals("Euro", updatedCurrency.getName());
        verify(currencyRepository, times(1)).save(currency);
    }

    @Test
    void testDeleteCurrency() {
        when(currencyRepository.findById(1L)).thenReturn(Optional.of(currency));

        boolean result = currencyService.deleteCurrency(1L);

        assertTrue(result);
        verify(currencyRepository, times(1)).delete(currency);
    }

    @Test
    void testGetCurrencyByCode() {
        when(currencyRepository.findByCode("USD")).thenReturn(currency);

        Currency foundCurrency = currencyService.getCurrencyByCode("USD");

        assertNotNull(foundCurrency);
        assertEquals("USD", foundCurrency.getCode());
    }

    @Test
    void testGetCurrencyByCode_NotFound() {
        when(currencyRepository.findByCode("USD")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> currencyService.getCurrencyByCode("USD"));

        assertEquals("Currency not found with code: USD", exception.getMessage());
    }

    @Test
    void testGetCurrencyByName() {
        when(currencyRepository.findByName("US Dollar")).thenReturn(currency);

        Currency foundCurrency = currencyService.getCurrencyByName("US Dollar");

        assertNotNull(foundCurrency);
        assertEquals("US Dollar", foundCurrency.getName());
    }

    @Test
    void testGetCurrencyBySymbol() {
        when(currencyRepository.findBySymbol("$")).thenReturn(currency);

        Currency foundCurrency = currencyService.getCurrencyBySymbol("$");

        assertNotNull(foundCurrency);
        assertEquals("$", foundCurrency.getSymbol());
    }

    @Test
    void testGetCurrencyByExchangeRate() {
        when(currencyRepository.findByExchangeRate(1.0)).thenReturn(currency);

        Currency foundCurrency = currencyService.getCurrencyByExchangeRate(1.0);

        assertNotNull(foundCurrency);
        assertEquals(1.0, foundCurrency.getExchangeRate());
    }
}

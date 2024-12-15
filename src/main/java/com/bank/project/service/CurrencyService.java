package com.bank.project.service;

import com.bank.project.entity.Currency;
import com.bank.project.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyService.class);

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    // Create new currency
    public Currency createCurrency(Currency currency) {
        currency.setCreatedAt(java.time.LocalDateTime.now());
        logger.info("Creating new currency with code: {}", currency.getCode());
        return currencyRepository.save(currency);
    }

    // Get currency by ID
    public Currency getCurrencyById(Long id) {
        logger.info("Searching for currency with ID: {}", id);
        Optional<Currency> currency = currencyRepository.findById(id);
        if (currency.isEmpty()) {
            logger.warn("Currency not found with ID: {}", id);
            throw new RuntimeException("Currency not found with ID: " + id);
        }
        logger.info("Currency found with ID: {} and code: {}", id, currency.get().getCode());
        return currency.get();
    }

    // Get all currencies
    public List<Currency> getAllCurrencies() {
        logger.debug("Fetching all currencies");
        return currencyRepository.findAll();
    }

    // Update currency data
    public Currency updateCurrency(Long id, Currency currencyDetails) {
        logger.info("Updating currency with ID: {}", id);
        Currency existingCurrency = getCurrencyById(id);
        existingCurrency.setCode(currencyDetails.getCode());
        existingCurrency.setName(currencyDetails.getName());
        existingCurrency.setSymbol(currencyDetails.getSymbol());
        existingCurrency.setExchangeRate(currencyDetails.getExchangeRate());
        existingCurrency.setUpdatedAt(java.time.LocalDateTime.now());
        logger.info("Currency with ID: {} updated to new code: {}", id, existingCurrency.getCode());
        return currencyRepository.save(existingCurrency);
    }

    // Delete currency
    public boolean deleteCurrency(Long id) {
        logger.info("Deleting currency with ID: {}", id);
        Currency currency = getCurrencyById(id);
        currencyRepository.delete(currency);
        logger.info("Currency with ID: {} deleted successfully", id);
            return false;
    }

    // Get currency by code
    public Currency getCurrencyByCode(String code) {
        logger.info("Searching for currency with code: {}", code);
        Currency currency = currencyRepository.findByCode(code);
        if (currency == null) {
            logger.warn("Currency not found with code: {}", code);
            throw new RuntimeException("Currency not found with code: " + code);
        }
        logger.info("Currency found with code: {}", code);
        return currency;
    }

    // Get currency by name
    public Currency getCurrencyByName(String name) {
        logger.info("Searching for currency with name: {}", name);
        Currency currency = currencyRepository.findByName(name);
        if (currency == null) {
            logger.warn("Currency not found with name: {}", name);
            throw new RuntimeException("Currency not found with name: " + name);
        }
        logger.info("Currency found with name: {}", name);
        return currency;
    }

    // Get currency by symbol
    public Currency getCurrencyBySymbol(String symbol) {
        logger.info("Searching for currency with symbol: {}", symbol);
        Currency currency = currencyRepository.findBySymbol(symbol);
        if (currency == null) {
            logger.warn("Currency not found with symbol: {}", symbol);
            throw new RuntimeException("Currency not found with symbol: " + symbol);
        }
        logger.info("Currency found with symbol: {}", symbol);
        return currency;
    }

    // Get currency by exchange rate
    public Currency getCurrencyByExchangeRate(Double exchangeRate) {
        logger.info("Searching for currency with exchange rate: {}", exchangeRate);
        Currency currency = currencyRepository.findByExchangeRate(exchangeRate);
        if (currency == null) {
            logger.warn("Currency not found with exchange rate: {}", exchangeRate);
            throw new RuntimeException("Currency not found with exchange rate: " + exchangeRate);
        }
        logger.info("Currency found with exchange rate: {}", exchangeRate);
        return currency;
    }
}

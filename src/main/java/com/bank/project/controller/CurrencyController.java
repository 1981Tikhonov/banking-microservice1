package com.bank.project.controller;

import com.bank.project.entity.Currency;
import com.bank.project.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Operation(summary = "Create a new currency", description = "Creates a new currency record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<Currency> createCurrency(@RequestBody Currency currency) {
        logger.info("Creating a new currency: {}", currency);
        Currency createdCurrency = currencyService.createCurrency(currency);
        logger.info("Currency created successfully with ID: {}", createdCurrency.getId());
        return ResponseEntity.ok(createdCurrency);
    }

    @Operation(summary = "Get currency by ID", description = "Fetches a currency by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency found"),
            @ApiResponse(responseCode = "404", description = "Currency not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Currency> getCurrencyById(@PathVariable Long id) {
        logger.info("Fetching currency with ID: {}", id);
        Currency currency = currencyService.getCurrencyById(id);
        if (currency != null) {
            logger.info("Currency found: {}", currency);
            return ResponseEntity.ok(currency);
        } else {
            logger.warn("Currency with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get all currencies", description = "Fetches a list of all currencies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currencies fetched successfully")
    })
    @GetMapping
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        logger.info("Fetching all currencies");
        List<Currency> currencies = currencyService.getAllCurrencies();
        logger.info("Total currencies fetched: {}", currencies.size());
        return ResponseEntity.ok(currencies);
    }

    @Operation(summary = "Update an existing currency", description = "Updates an existing currency record by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency updated successfully"),
            @ApiResponse(responseCode = "404", description = "Currency not found for update")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Currency> updateCurrency(@PathVariable Long id, @RequestBody Currency currency) {
        logger.info("Updating currency with ID: {}", id);
        Currency updatedCurrency = currencyService.updateCurrency(id, currency);
        if (updatedCurrency != null) {
            logger.info("Currency updated successfully: {}", updatedCurrency);
            return ResponseEntity.ok(updatedCurrency);
        } else {
            logger.warn("Currency with ID: {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a currency", description = "Deletes a currency by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Currency deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Currency not found for deletion")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
        logger.info("Attempting to delete currency with ID: {}", id);
        boolean isDeleted = currencyService.deleteCurrency(id);
        if (isDeleted) {
            logger.info("Currency with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Currency with ID: {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get currency by code", description = "Fetches a currency by its unique code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency found"),
            @ApiResponse(responseCode = "404", description = "Currency with code not found")
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<Currency> getCurrencyByCode(@PathVariable String code) {
        logger.info("Fetching currency with code: {}", code);
        Currency currency = currencyService.getCurrencyByCode(code);
        if (currency != null) {
            logger.info("Currency found: {}", currency);
            return ResponseEntity.ok(currency);
        } else {
            logger.warn("Currency with code: {} not found", code);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get currency by name", description = "Fetches a currency by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency found"),
            @ApiResponse(responseCode = "404", description = "Currency with name not found")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<Currency> getCurrencyByName(@PathVariable String name) {
        logger.info("Fetching currency with name: {}", name);
        Currency currency = currencyService.getCurrencyByName(name);
        if (currency != null) {
            logger.info("Currency found: {}", currency);
            return ResponseEntity.ok(currency);
        } else {
            logger.warn("Currency with name: {} not found", name);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get currency by symbol", description = "Fetches a currency by its symbol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency found"),
            @ApiResponse(responseCode = "404", description = "Currency with symbol not found")
    })
    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<Currency> getCurrencyBySymbol(@PathVariable String symbol) {
        logger.info("Fetching currency with symbol: {}", symbol);
        Currency currency = currencyService.getCurrencyBySymbol(symbol);
        if (currency != null) {
            logger.info("Currency found: {}", currency);
            return ResponseEntity.ok(currency);
        } else {
            logger.warn("Currency with symbol: {} not found", symbol);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get currency by exchange rate", description = "Fetches a currency by its exchange rate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency found"),
            @ApiResponse(responseCode = "404", description = "Currency with exchange rate not found")
    })
    @GetMapping("/exchange-rate/{exchangeRate}")
    public ResponseEntity<Currency> getCurrencyByExchangeRate(@PathVariable Double exchangeRate) {
        logger.info("Fetching currency with exchange rate: {}", exchangeRate);
        Currency currency = currencyService.getCurrencyByExchangeRate(exchangeRate);
        if (currency != null) {
            logger.info("Currency found: {}", currency);
            return ResponseEntity.ok(currency);
        } else {
            logger.warn("Currency with exchange rate: {} not found", exchangeRate);
            return ResponseEntity.notFound().build();
        }
    }
}

package com.bank.project.controller;

import com.bank.project.entity.Account;
import com.bank.project.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Management", description = "Endpoints for managing bank accounts")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Create a new account", description = "Creates a new bank account for a specific client.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account successfully created", content = @Content(schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Account> createAccount(
            @RequestBody(description = "Details of the account to create", required = true,
                    content = @Content(schema = @Schema(implementation = Account.class)))
            @org.springframework.web.bind.annotation.RequestBody Account account) {
        logger.info("Creating account for client with ID: {}", account.getClientId());
        Account createdAccount = accountService.createAccount(account);
        return ResponseEntity.status(201).body(createdAccount);
    }

    @Operation(summary = "Get account by ID", description = "Retrieves account details by account ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account found", content = @Content(schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(
            @Parameter(description = "ID of the account to retrieve", required = true)
            @PathVariable Long id) {
        return accountService.getAccountById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all accounts", description = "Fetches all bank accounts.")
    @ApiResponse(responseCode = "200", description = "List of accounts retrieved", content = @Content(schema = @Schema(implementation = Account.class)))
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get accounts by status", description = "Fetches accounts by their status.")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Account>> getAccountsByStatus(
            @Parameter(description = "Status of the accounts to fetch", required = true)
            @PathVariable String status) {
        List<Account> accounts = accountService.getAccountsByStatus(status);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get accounts with balance less than a specified amount", description = "Fetches accounts whose balance is less than the given amount.")
    @GetMapping("/balance/lessThan/{balance}")
    public ResponseEntity<List<Account>> getAccountsByBalanceLessThan(
            @Parameter(description = "Maximum balance threshold", required = true)
            @PathVariable BigDecimal balance) {
        List<Account> accounts = accountService.getAccountsByBalanceLessThan(balance);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get accounts by currency code", description = "Fetches accounts with a specific currency code.")
    @GetMapping("/currency/{currencyCode}")
    public ResponseEntity<List<Account>> getAccountsByCurrencyCode(
            @Parameter(description = "Currency code of the accounts to fetch", required = true)
            @PathVariable String currencyCode) {
        List<Account> accounts = accountService.getAccountsByCurrencyCode(currencyCode);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get accounts created within a date range", description = "Fetches accounts created between the specified start and end dates.")
    @GetMapping("/createdAtBetween")
    public ResponseEntity<List<Account>> getAccountsByCreatedAtBetween(
            @Parameter(description = "Start date of the range", required = true)
            @RequestParam LocalDateTime startDate,
            @Parameter(description = "End date of the range", required = true)
            @RequestParam LocalDateTime endDate) {
        List<Account> accounts = accountService.getAccountsByCreatedAtBetween(startDate, endDate);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get accounts updated after a specific date", description = "Fetches accounts updated after the specified date.")
    @GetMapping("/updatedAtAfter/{updatedAt}")
    public ResponseEntity<List<Account>> getAccountsByUpdatedAtAfter(
            @Parameter(description = "Date to filter accounts updated after", required = true)
            @PathVariable LocalDateTime updatedAt) {
        List<Account> accounts = accountService.getAccountsByUpdatedAtAfter(updatedAt);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get account by name", description = "Fetches an account by its name.")
    @GetMapping("/name/{name}")
    public ResponseEntity<Account> getAccountByName(
            @Parameter(description = "Name of the account to fetch", required = true)
            @PathVariable String name) {
        return accountService.getAccountByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update an account", description = "Updates the details of an existing account by ID.")
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(
            @Parameter(description = "ID of the account to update", required = true)
            @PathVariable Long id,
            @RequestBody(description = "Updated account details", required = true,
                    content = @Content(schema = @Schema(implementation = Account.class)))
            @org.springframework.web.bind.annotation.RequestBody Account account) {
        Account updatedAccount = accountService.updateAccount(id, account);
        if (updatedAccount != null) {
            return ResponseEntity.ok(updatedAccount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete an account", description = "Deletes an account by its ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @Parameter(description = "ID of the account to delete", required = true)
            @PathVariable Long id) {
        boolean deleted = accountService.deleteAccount(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

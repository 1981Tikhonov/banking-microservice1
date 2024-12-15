package com.bank.project.controller;

import com.bank.project.entity.Transaction;
import com.bank.project.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    @Operation(summary = "Create a new transaction", description = "Create a new transaction in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        logger.info("Received request to create transaction: {}", transaction);
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        logger.info("Transaction created successfully: {}", createdTransaction);
        return ResponseEntity.ok(createdTransaction);
    }

    @Operation(summary = "Get transaction by ID", description = "Retrieve a transaction by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        logger.info("Fetching transaction with ID: {}", id);
        Transaction transaction = transactionService.getTransactionById(id);
        logger.info("Transaction retrieved: {}", transaction);
        return ResponseEntity.ok(transaction);
    }

    @Operation(summary = "Get all transactions", description = "Retrieve a list of all transactions")
    @ApiResponse(responseCode = "200", description = "List of transactions retrieved successfully")
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        logger.info("Fetching all transactions");
        List<Transaction> transactions = transactionService.getAllTransactions();
        logger.info("Retrieved {} transactions", transactions.size());
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Filter transactions by debit account ID", description = "Retrieve transactions filtered by debit account ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered transactions found"),
            @ApiResponse(responseCode = "404", description = "No transactions found")
    })
    @GetMapping("/filter-by-debit-account")
    public ResponseEntity<List<Transaction>> getTransactionsByDebitAccountId(
            @Parameter(description = "Debit account ID to filter transactions") @RequestParam Long debitAccountId) {
        logger.info("Filtering transactions by debit account ID: {}", debitAccountId);
        List<Transaction> transactions = transactionService.getTransactionsByDebitAccountId(debitAccountId);
        logger.info("Found {} transactions for debit account ID: {}", transactions.size(), debitAccountId);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Filter transactions by credit account ID", description = "Retrieve transactions filtered by credit account ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered transactions found"),
            @ApiResponse(responseCode = "404", description = "No transactions found")
    })
    @GetMapping("/filter-by-credit-account")
    public ResponseEntity<List<Transaction>> getTransactionsByCreditAccountId(
            @Parameter(description = "Credit account ID to filter transactions") @RequestParam Long creditAccountId) {
        logger.info("Filtering transactions by credit account ID: {}", creditAccountId);
        List<Transaction> transactions = transactionService.getTransactionsByCreditAccountId(creditAccountId);
        logger.info("Found {} transactions for credit account ID: {}", transactions.size(), creditAccountId);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Filter transactions by type", description = "Retrieve transactions filtered by type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered transactions found"),
            @ApiResponse(responseCode = "404", description = "No transactions found")
    })
    @GetMapping("/filter-by-type")
    public ResponseEntity<List<Transaction>> getTransactionsByType(
            @Parameter(description = "Transaction type to filter") @RequestParam String type) {
        logger.info("Filtering transactions by type: {}", type);
        List<Transaction> transactions = transactionService.getTransactionsByType(type);
        logger.info("Found {} transactions for type: {}", transactions.size(), type);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Filter transactions by amount", description = "Retrieve transactions filtered by amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered transactions found"),
            @ApiResponse(responseCode = "404", description = "No transactions found")
    })
    @GetMapping("/filter-by-amount")
    public ResponseEntity<List<Transaction>> getTransactionsByAmount(
            @Parameter(description = "Amount to filter transactions by") @RequestParam Double amount) {
        logger.info("Filtering transactions by amount: {}", amount);
        List<Transaction> transactions = transactionService.getTransactionsByAmount(amount);
        logger.info("Found {} transactions with amount: {}", transactions.size(), amount);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Filter transactions by date range", description = "Retrieve transactions filtered by a date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered transactions found"),
            @ApiResponse(responseCode = "404", description = "No transactions found")
    })
    @GetMapping("/filter-by-date-range")
    public ResponseEntity<List<Transaction>> getTransactionsByCreatedAtBetween(
            @Parameter(description = "Start date of the date range") @RequestParam LocalDateTime startDate,
            @Parameter(description = "End date of the date range") @RequestParam LocalDateTime endDate) {
        logger.info("Filtering transactions by date range: {} to {}", startDate, endDate);
        List<Transaction> transactions = transactionService.getTransactionsByCreatedAtBetween(startDate, endDate);
        logger.info("Found {} transactions in the date range: {} to {}", transactions.size(), startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Delete a transaction", description = "Delete a transaction by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transaction deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        logger.info("Request to delete transaction with ID: {}", id);
        transactionService.deleteTransaction(id);
        logger.info("Transaction with ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}

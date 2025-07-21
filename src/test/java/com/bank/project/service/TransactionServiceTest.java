package com.bank.project.service;

import com.bank.project.entity.Transaction;
import com.bank.project.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Создание mock транзакции для тестов
        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setDebitAccountId(101L);
        transaction.setCreditAccountId(102L);
        transaction.setType("Transfer");
        transaction.setAmount(BigDecimal.valueOf(1000.0));
        transaction.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateTransaction() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction createdTransaction = transactionService.createTransaction(transaction);

        assertNotNull(createdTransaction);
        assertEquals(1L, createdTransaction.getId());
        assertEquals(1000.0, createdTransaction.getAmount());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_InvalidAmount() {
        transaction.setAmount(BigDecimal.valueOf(-1000.0));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> transactionService.createTransaction(transaction));

        assertEquals("Transaction amount must be greater than 0.", exception.getMessage());
    }

    @Test
    void testGetTransactionById() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        Transaction foundTransaction = transactionService.getTransactionById(1L);

        assertNotNull(foundTransaction);
        assertEquals(1L, foundTransaction.getId());
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTransactionById_NotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> transactionService.getTransactionById(1L));

        assertEquals("Transaction not found with ID: 1", exception.getMessage());
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllTransactions() {
        when(transactionRepository.findAll()).thenReturn(List.of(transaction));

        List<Transaction> transactions = transactionService.getAllTransactions();

        assertFalse(transactions.isEmpty());
        assertEquals(1, transactions.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testDeleteTransaction() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(1L);

        verify(transactionRepository, times(1)).delete(transaction);
    }

    @Test
    void testDeleteTransaction_NotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> transactionService.deleteTransaction(1L));

        assertEquals("Transaction not found with ID: 1", exception.getMessage());
        verify(transactionRepository, times(0)).delete(any(Transaction.class));
    }

    @Test
    void testGetTransactionsByDebitAccountId() {
        when(transactionRepository.findByDebitAccountId(101L)).thenReturn(List.of(transaction));

        List<Transaction> transactions = transactionService.getTransactionsByDebitAccountId(101L);

        assertFalse(transactions.isEmpty());
        assertEquals(1, transactions.size());
        verify(transactionRepository, times(1)).findByDebitAccountId(101L);
    }

    @Test
    void testGetTransactionsByCreditAccountId() {
        when(transactionRepository.findByCreditAccountId(102L)).thenReturn(List.of(transaction));

        List<Transaction> transactions = transactionService.getTransactionsByCreditAccountId(102L);

        assertFalse(transactions.isEmpty());
        assertEquals(1, transactions.size());
        verify(transactionRepository, times(1)).findByCreditAccountId(102L);
    }

    @Test
    void testGetTransactionsByType() {
        when(transactionRepository.findByType("Transfer")).thenReturn(List.of(transaction));

        List<Transaction> transactions = transactionService.getTransactionsByType("Transfer");

        assertFalse(transactions.isEmpty());
        assertEquals(1, transactions.size());
        verify(transactionRepository, times(1)).findByType("Transfer");
    }

    @Test
    void testGetTransactionsByAmount() {
        when(transactionRepository.findByAmount(1000.0)).thenReturn(List.of(transaction));

        List<Transaction> transactions = transactionService.getTransactionsByAmount(1000.0);

        assertFalse(transactions.isEmpty());
        assertEquals(1, transactions.size());
        verify(transactionRepository, times(1)).findByAmount(1000.0);
    }

    @Test
    void testGetTransactionsByCreatedAtBetween() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(transactionRepository.findByCreatedAtBetween(startDate, endDate)).thenReturn(List.of(transaction));

        List<Transaction> transactions = transactionService.getTransactionsByCreatedAtBetween(startDate, endDate);

        assertFalse(transactions.isEmpty());
        assertEquals(1, transactions.size());
        verify(transactionRepository, times(1)).findByCreatedAtBetween(startDate, endDate);
    }
}

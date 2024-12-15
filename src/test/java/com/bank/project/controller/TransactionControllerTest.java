package com.bank.project.controller;

import com.bank.project.entity.Transaction;
import com.bank.project.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createTransaction_ShouldReturnTransaction() throws Exception {
        Transaction transaction = new Transaction(1L, 100L, 200L, "TRANSFER", 500.0, LocalDateTime.now(), LocalDateTime.now());
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transaction.getId()))
                .andExpect(jsonPath("$.type").value(transaction.getType()))
                .andExpect(jsonPath("$.amount").value(transaction.getAmount()));

        verify(transactionService, times(1)).createTransaction(any(Transaction.class));
    }

    @Test
    void getTransactionById_ShouldReturnTransaction() throws Exception {
        Transaction transaction = new Transaction(1L, 100L, 200L, "TRANSFER", 500.0, LocalDateTime.now(), LocalDateTime.now());
        when(transactionService.getTransactionById(1L)).thenReturn(transaction);

        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transaction.getId()))
                .andExpect(jsonPath("$.type").value(transaction.getType()))
                .andExpect(jsonPath("$.amount").value(transaction.getAmount()));

        verify(transactionService, times(1)).getTransactionById(1L);
    }

    @Test
    void getAllTransactions_ShouldReturnTransactions() throws Exception {
        Transaction transaction1 = new Transaction(1L, 100L, 200L, "TRANSFER", 500.0, LocalDateTime.now(), LocalDateTime.now());
        Transaction transaction2 = new Transaction(2L, 101L, 201L, "DEPOSIT", 1000.0, LocalDateTime.now(), LocalDateTime.now());
        when(transactionService.getAllTransactions()).thenReturn(Arrays.asList(transaction1, transaction2));

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(transactionService, times(1)).getAllTransactions();
    }

    @Test
    void getTransactionsByDebitAccountId_ShouldReturnTransactions() throws Exception {
        Transaction transaction = new Transaction(1L, 100L, 200L, "TRANSFER", 500.0, LocalDateTime.now(), LocalDateTime.now());
        when(transactionService.getTransactionsByDebitAccountId(100L)).thenReturn(List.of(transaction));

        mockMvc.perform(get("/api/transactions/filter-by-debit-account")
                        .param("debitAccountId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(transactionService, times(1)).getTransactionsByDebitAccountId(100L);
    }

    @Test
    void getTransactionsByType_ShouldReturnTransactions() throws Exception {
        Transaction transaction = new Transaction(1L, 100L, 200L, "TRANSFER", 500.0, LocalDateTime.now(), LocalDateTime.now());
        when(transactionService.getTransactionsByType("TRANSFER")).thenReturn(List.of(transaction));

        mockMvc.perform(get("/api/transactions/filter-by-type")
                        .param("type", "TRANSFER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(transactionService, times(1)).getTransactionsByType("TRANSFER");
    }

    @Test
    void deleteTransaction_ShouldReturnNoContent() throws Exception {
        doNothing().when(transactionService).deleteTransaction(1L);

        mockMvc.perform(delete("/api/transactions/1"))
                .andExpect(status().isNoContent());

        verify(transactionService, times(1)).deleteTransaction(1L);
    }
}

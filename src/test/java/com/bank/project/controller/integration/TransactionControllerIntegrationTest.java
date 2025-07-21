package com.bank.project.controller.integration;

import com.bank.project.entity.Account;
import com.bank.project.entity.AccountStatus;
import com.bank.project.entity.Client;
import com.bank.project.entity.Transaction;
import com.bank.project.repository.AccountRepository;
import com.bank.project.repository.ClientRepository;
import com.bank.project.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TransactionControllerIntegrationTest {

    private static final String ACTIVE =    "ACTIVE";
    private static final Integer USD =       840;
    private static final String TRANSFER =  "TRANSFER";
    private static final String DEPOSIT =           "DEPOSIT";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Account sourceAccount;
    private Account targetAccount;
    private Client client;
    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        // Clean up before each test
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        clientRepository.deleteAll();

        // Create test client
        client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john.doe@example.com");
        client.setStatus("ACTIVE");
        client = clientRepository.save(client);

        // Create source account
        sourceAccount = new Account();
        sourceAccount.setAccountNumber("ACC123456789");
        sourceAccount.setClient(client);
        sourceAccount.setBalance(new BigDecimal("1000.00"));
        sourceAccount.setCurrencyCode(USD);
        sourceAccount.setStatus(AccountStatus.valueOf(ACTIVE));
        sourceAccount = accountRepository.save(sourceAccount);

        // Create target account
        targetAccount = new Account();
        targetAccount.setAccountNumber("ACC987654321");
        targetAccount.setClient(client);
        targetAccount.setBalance(new BigDecimal("500.00"));
        targetAccount.setCurrencyCode(USD);
        targetAccount.setStatus(AccountStatus.valueOf(ACTIVE));
        targetAccount = accountRepository.save(targetAccount);

        // Create test transaction
        testTransaction = new Transaction();
        testTransaction.setSourceAccount(sourceAccount);
        testTransaction.setTargetAccount(targetAccount);
        testTransaction.setAmount(new BigDecimal("100.00"));
        testTransaction.setType(TRANSFER);
        testTransaction.setDescription("Test transfer");
        testTransaction = transactionRepository.save(testTransaction);
    }

    @Test
    void createTransaction_ShouldCreateTransaction() throws Exception {
        String transactionJson = String.format("""
            {
                "sourceAccountId": %d,
                "targetAccountId": %d,
                "amount": 200.00,
                "type": "TRANSFER",
                "description": "Test transfer"
            }
            """, sourceAccount.getId(), targetAccount.getId());

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(200.00))
                .andExpect(jsonPath("$.type").value("TRANSFER"));

        // Verify account balances were updated
        mockMvc.perform(get("/api/accounts/{id}", sourceAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(800.00));

        mockMvc.perform(get("/api/accounts/{id}", targetAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(700.00));
    }

    @Test
    void getTransactionById_ShouldReturnTransaction() throws Exception {
        mockMvc.perform(get("/api/transactions/{id}", testTransaction.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testTransaction.getId()))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.type").value("TRANSFER"));
    }

    @Test
    void getTransactionsByAccount_ShouldReturnTransactions() throws Exception {
        // Create another transaction
        Transaction anotherTransaction = new Transaction();
        anotherTransaction.setSourceAccount(sourceAccount);
        anotherTransaction.setTargetAccount(targetAccount);
        anotherTransaction.setAmount(new BigDecimal("50.00"));
        anotherTransaction.setType(TRANSFER);
        transactionRepository.save(anotherTransaction);

        mockMvc.perform(get("/api/transactions/account/{accountId}", sourceAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getTransactionsByPeriod_ShouldReturnFilteredTransactions() throws Exception {
        // Create transaction with specific date
        Transaction oldTransaction = new Transaction();
        oldTransaction.setSourceAccount(sourceAccount);
        oldTransaction.setTargetAccount(targetAccount);
        oldTransaction.setAmount(new BigDecimal("75.00"));
        oldTransaction.setType(TransactionType.DEPOSIT.name());  // Properly convert enum to string
        oldTransaction.setCreatedAt(LocalDateTime.now().minusDays(10));
        transactionRepository.save(oldTransaction);

        String startDate = LocalDateTime.now().minusDays(5).toString();
        String endDate = LocalDateTime.now().plusDays(1).toString();

        mockMvc.perform(get("/api/transactions/period")
                .param("startDate", startDate)
                .param("endDate", endDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))); // Only testTransaction should be in this period
    }

    @Test
    void getTransactionsByType_ShouldReturnFilteredTransactions() throws Exception {
        // Create a deposit transaction
        Transaction depositTransaction = new Transaction();
        depositTransaction.setTargetAccount(sourceAccount);
        depositTransaction.setAmount(new BigDecimal("200.00"));
        depositTransaction.setType(DEPOSIT);
        transactionRepository.save(depositTransaction);

        mockMvc.perform(get("/api/transactions/type/DEPOSIT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type").value("DEPOSIT"));
    }

    @Test
    void deleteTransaction_ShouldDeleteTransaction() throws Exception {
        mockMvc.perform(delete("/api/transactions/{id}", testTransaction.getId()))
                .andExpect(status().isNoContent());

        // Verify transaction is deleted
        mockMvc.perform(get("/api/transactions/{id}", testTransaction.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTransaction_WithInsufficientFunds_ShouldReturnBadRequest() throws Exception {
        String transactionJson = String.format("""
            {
                "sourceAccountId": %d,
                "targetAccountId": %d,
                "amount": 10000.00,
                "type": "TRANSFER",
                "description": "Large transfer"
            }
            """, sourceAccount.getId(), targetAccount.getId());

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionJson))
                .andExpect(status().isBadRequest());
    }
}

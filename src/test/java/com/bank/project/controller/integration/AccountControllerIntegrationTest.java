package com.bank.project.controller.integration;

import com.bank.project.entity.Account;
import com.bank.project.entity.AccountStatus;
import com.bank.project.entity.Client;
import com.bank.project.repository.AccountRepository;
import com.bank.project.repository.ClientRepository;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Client testClient;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        // Clean up before each test
        accountRepository.deleteAll();
        clientRepository.deleteAll();

        // Create test client
        testClient = new Client();
        testClient.setFirstName("John");
        testClient.setLastName("Doe");
        testClient.setEmail("john.doe@example.com");
        testClient.setStatus("ACTIVE");
        testClient = clientRepository.save(testClient);

        // Create test account
        testAccount = new Account();
        testAccount.setAccountNumber("ACC123456789");
        testAccount.setName("Test Account");
        testAccount.setType("CHECKING");
        testAccount.setClient(testClient);
        testAccount.setBalance(new BigDecimal("1000.00"));
        testAccount.setCurrencyCode(840); // USD currency code
        testAccount.setStatus(AccountStatus.valueOf("ACTIVE"));
        testAccount = accountRepository.save(testAccount);
    }

    @Test
    void createAccount_ShouldReturnCreatedAccount() throws Exception {
        String accountJson = String.format("""
            {
                "accountNumber": "ACC987654321",
                "clientId": %d,
                "balance": 500.00,
                "currency": "EUR",
                "status": "ACTIVE"
            }
            """, testClient.getId());

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value("ACC987654321"))
                .andExpect(jsonPath("$.balance").value(500.00))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void getAccountById_ShouldReturnAccount() throws Exception {
        mockMvc.perform(get("/api/accounts/{id}", testAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testAccount.getId()))
                .andExpect(jsonPath("$.accountNumber").value("ACC123456789"))
                .andExpect(jsonPath("$.balance").value(1000.00));
    }

    @Test
    void updateAccount_ShouldUpdateAccount() throws Exception {
        String updateJson = """
            {
                "balance": 1500.00,
                "status": "BLOCKED"
            }
            """;

        mockMvc.perform(put("/api/accounts/{id}", testAccount.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1500.00))
                .andExpect(jsonPath("$.status").value("BLOCKED"));
    }

    @Test
    void deleteAccount_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/accounts/{id}", testAccount.getId()))
                .andExpect(status().isNoContent());

        // Verify account is deleted
        mockMvc.perform(get("/api/accounts/{id}", testAccount.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deposit_ShouldIncreaseBalance() throws Exception {
        mockMvc.perform(post("/api/accounts/{id}/deposit", testAccount.getId())
                .param("amount", "500.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1500.00));
    }

    @Test
    void withdraw_WithSufficientFunds_ShouldDecreaseBalance() throws Exception {
        mockMvc.perform(post("/api/accounts/{id}/withdraw", testAccount.getId())
                .param("amount", "500.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(500.00));
    }

    @Test
    void withdraw_WithInsufficientFunds_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/accounts/{id}/withdraw", testAccount.getId())
                .param("amount", "1500.00"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void transfer_ShouldTransferAmountBetweenAccounts() throws Exception {
        // Create target account
        Account targetAccount = new Account();
        targetAccount.setAccountNumber("ACC999999999");
        targetAccount.setName("Target Account");
        targetAccount.setType("SAVINGS");
        targetAccount.setClient(testClient);
        targetAccount.setBalance(new BigDecimal("500.00"));
        targetAccount.setCurrencyCode(840); // USD currency code
        targetAccount.setStatus(AccountStatus.valueOf("ACTIVE"));
        targetAccount = accountRepository.save(targetAccount);

        mockMvc.perform(post("/api/accounts/transfer")
                .param("fromAccountId", testAccount.getId().toString())
                .param("toAccountId", targetAccount.getId().toString())
                .param("amount", "300.00"))
                .andExpect(status().isOk());

        // Verify balances after transfer
        mockMvc.perform(get("/api/accounts/{id}", testAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(700.00));

        mockMvc.perform(get("/api/accounts/{id}", targetAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(800.00));
    }

    @Test
    void getAccountsByClientId_ShouldReturnClientAccounts() throws Exception {
        // Create another account for the same client
        Account anotherAccount = new Account();
        anotherAccount.setAccountNumber("ACC111111111");
        anotherAccount.setName("Another Account");
        anotherAccount.setType("INVESTMENT");
        anotherAccount.setClient(testClient);
        anotherAccount.setBalance(new BigDecimal("2000.00"));
        anotherAccount.setCurrencyCode(840); // USD currency code
        anotherAccount.setStatus(AccountStatus.valueOf("ACTIVE"));
        accountRepository.save(anotherAccount);

        mockMvc.perform(get("/api/accounts/client/{clientId}", testClient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[?(@.accountNumber == 'ACC123456789')]").exists())
                .andExpect(jsonPath("$[?(@.accountNumber == 'ACC111111111')]").exists());
    }
}

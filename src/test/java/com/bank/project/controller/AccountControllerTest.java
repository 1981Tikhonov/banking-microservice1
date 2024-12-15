package com.bank.project.controller;

import com.bank.project.entity.Account;
import com.bank.project.entity.Currency;
import com.bank.project.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    public void testCreateAccount() throws Exception {
        Account account = new Account();
        account.setName("account");
        account.setClientId(1L);
        account.setBalance(BigDecimal.valueOf(1000));
        account.setCurrencyCode(Integer.valueOf("USD"));
        account.setStatus("ACTIVE");

        when(accountService.createAccount(Mockito.any(Account.class))).thenReturn(account);

        mockMvc.perform(post("/api/accounts")
                        .contentType("application/json")
                        .content("{\"clientId\": 1, \"balance\": 1000, \"currencyCode\": \"USD\", \"status\": \"ACTIVE\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.balance").value(1000))
                .andExpect(jsonPath("$.currencyCode").value("USD"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(accountService, times(1)).createAccount(Mockito.any(Account.class));
    }

    @Test
    public void testGetAccountById() throws Exception {
        Account account = new Account();
        account.setId(1L);
        account.setClientId(1L);
        account.setBalance(BigDecimal.valueOf(1000));
        account.setCurrencyCode(Currency.CurrencyCode.USD.ordinal());
        account.setStatus("ACTIVE");

        when(accountService.getAccountById(1L)).thenReturn(Optional.of(account));

        mockMvc.perform(get("/api/accounts/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.balance").value(1000))
                .andExpect(jsonPath("$.currencyCode").value("USD"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(accountService, times(1)).getAccountById(1L);
    }

    @Test
    public void testGetAccountById_NotFound() throws Exception {
        when(accountService.getAccountById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/accounts/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).getAccountById(1L);
    }

    @Test
    public void testGetAllAccounts() throws Exception {
        Account account1 = new Account();
        account1.setId(1L);
        account1.setClientId(1L);
        account1.setBalance(BigDecimal.valueOf(1000));
        account1.setCurrencyCode(Integer.valueOf("USD"));
        account1.setStatus("ACTIVE");

        Account account2 = new Account();
        account2.setId(2L);
        account2.setClientId(2L);
        account2.setBalance(BigDecimal.valueOf(500));
        account2.setCurrencyCode(Integer.valueOf("EUR"));
        account2.setStatus("ACTIVE");

        when(accountService.getAllAccounts()).thenReturn(Arrays.asList(account1, account2));

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(accountService, times(1)).getAllAccounts();
    }

    @Test
    public void testUpdateAccount() throws Exception {
        Account account = new Account();
        account.setId(1L);
        account.setClientId(1L);
        account.setBalance(BigDecimal.valueOf(1000));
        account.setCurrencyCode(Integer.valueOf("USD"));
        account.setStatus("ACTIVE");

        when(accountService.updateAccount(eq(1L), Mockito.any(Account.class))).thenReturn(account);

        mockMvc.perform(put("/api/accounts/{id}", 1L)
                        .contentType("application/json")
                        .content("{\"balance\": 1500, \"currencyCode\": \"USD\", \"status\": \"ACTIVE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1500));

        verify(accountService, times(1)).updateAccount(eq(1L), Mockito.any(Account.class));
    }

    @Test
    public void testDeleteAccount() throws Exception {
        when(accountService.deleteAccount(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/accounts/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(accountService, times(1)).deleteAccount(1L);
    }

    @Test
    public void testDeleteAccount_NotFound() throws Exception {
        when(accountService.deleteAccount(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/accounts/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).deleteAccount(1L);
    }
}

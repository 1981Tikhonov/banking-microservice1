package com.bank.project.service;

import com.bank.project.entity.Account;
import com.bank.project.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        account = new Account();
        account.setId(1L);
        account.setName("Test Account");
        account.setClientId(1L);
        account.setBalance(BigDecimal.valueOf(1000));
        account.setCurrencyCode(Integer.valueOf("USD"));
        account.setStatus("ACTIVE");
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account createdAccount = accountService.createAccount(account);

        assertNotNull(createdAccount);
        assertEquals(account.getName(), createdAccount.getName());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testGetAccountById() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        Optional<Account> fetchedAccount = accountService.getAccountById(1L);

        assertTrue(fetchedAccount.isPresent());
        assertEquals(account.getId(), fetchedAccount.get().getId());
        verify(accountRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetAccountByIdNotFound() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Account> fetchedAccount = accountService.getAccountById(999L);

        assertFalse(fetchedAccount.isPresent());
        verify(accountRepository, times(1)).findById(anyLong());
    }

    @Test
    void testUpdateAccount() {
        when(accountRepository.existsById(anyLong())).thenReturn(true);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account updatedAccount = accountService.updateAccount(1L, account);

        assertNotNull(updatedAccount);
        assertEquals(account.getId(), updatedAccount.getId());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testUpdateAccountNotFound() {
        when(accountRepository.existsById(anyLong())).thenReturn(false);

        Account updatedAccount = accountService.updateAccount(999L, account);

        assertNull(updatedAccount);
        verify(accountRepository, times(0)).save(any(Account.class));
    }

    @Test
    void testDeleteAccount() {
        when(accountRepository.existsById(anyLong())).thenReturn(true);

        boolean isDeleted = accountService.deleteAccount(1L);

        assertTrue(isDeleted);
        verify(accountRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteAccountNotFound() {
        when(accountRepository.existsById(anyLong())).thenReturn(false);

        boolean isDeleted = accountService.deleteAccount(999L);

        assertFalse(isDeleted);
        verify(accountRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void testGetAccountsByStatus() {
        when(accountRepository.findAllByStatus(anyString())).thenReturn(List.of(account));

        List<Account> accounts = accountService.getAccountsByStatus("ACTIVE");

        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
        verify(accountRepository, times(1)).findAllByStatus(anyString());
    }
}

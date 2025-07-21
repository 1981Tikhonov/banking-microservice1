package com.bank.project.service;

import com.bank.project.entity.Account;
import com.bank.project.entity.AccountStatus;
import com.bank.project.entity.Client;
import com.bank.project.exception.InsufficientFundsException;
import com.bank.project.exception.ResourceNotFoundException;
import com.bank.project.repository.AccountRepository;
import com.bank.project.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AccountServiceImplTest {

    private static final Integer USD = 840;
    private static final String ACTIVE = "ACTIVE";
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account testAccount;
    private Client testClient;

    @BeforeEach
    void setUp() {
        testClient = new Client();
        testClient.setId(1L);
        testClient.setFirstName("John");
        testClient.setLastName("Doe");
        testClient.setStatus("ACTIVE");

        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setAccountNumber("ACC123456789");
        testAccount.setClient(testClient);
        testAccount.setBalance(new BigDecimal("1000.00"));
        testAccount.setCurrencyCode(USD);
        testAccount.setStatus(AccountStatus.valueOf(ACTIVE));
    }

    @Test
    void createAccount_ShouldReturnCreatedAccount() {
        // Arrange
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            account.setId(1L);
            return account;
        });

        // Act
        Account createdAccount = accountService.createAccount(testAccount);

        // Assert
        assertNotNull(createdAccount);
        assertEquals("ACC123456789", createdAccount.getAccountNumber());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createAccount_WhenClientNotExists_ShouldThrowException() {
        // Arrange
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> accountService.createAccount(testAccount));
    }

    @Test
    void getAccountById_WhenAccountExists_ShouldReturnAccount() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        // Act
        Account foundAccount = accountService.getAccountById(1L);

        // Assert
        assertNotNull(foundAccount);
        assertEquals("ACC123456789", foundAccount.getAccountNumber());
    }

    @Test
    void getAccountById_WhenAccountNotExists_ShouldThrowException() {
        // Arrange
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> accountService.getAccountById(999L));
    }

    @Test
    void getAllAccounts_ShouldReturnPageOfAccounts() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("accountNumber"));
        Page<Account> page = new PageImpl<>(List.of(testAccount));
        when(accountRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<Account> result = accountService.getAllAccounts(0, 10, new String[]{"accountNumber,asc"});

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("ACC123456789", result.getContent().get(0).getAccountNumber());
    }

    @Test
    void getAccountsByClientId_ShouldReturnClientAccounts() {
        // Arrange
        when(accountRepository.findAllByClientId(1L)).thenReturn(List.of(testAccount));

        // Act
        List<Account> accounts = accountService.getAccountsByClientId(1L);

        // Assert
        assertFalse(accounts.isEmpty());
        assertEquals(1, accounts.size());
        assertEquals("ACC123456789", accounts.get(0).getAccountNumber());
    }

    @Test
    void updateAccount_WhenAccountExists_ShouldUpdateAccount() {
        // Arrange
        Account updatedAccount = new Account();
        updatedAccount.setBalance(new BigDecimal("2000.00"));
        updatedAccount.setStatus(AccountStatus.BLOCKED);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Account result = accountService.updateAccount(1L, updatedAccount);

        // Assert
        assertNotNull(result);
        assertEquals(0, new BigDecimal("2000.00").compareTo(result.getBalance()));
        assertEquals(AccountStatus.BLOCKED, result.getStatus());
    }

    @Test
    void deleteAccount_WhenAccountExists_ShouldDeleteAccount() {
        // Arrange
        when(accountRepository.existsById(1L)).thenReturn(true);
        doNothing().when(accountRepository).deleteById(1L);

        // Act
        boolean result = accountService.deleteAccount(1L);

        // Assert
        assertTrue(result);
        verify(accountRepository, times(1)).deleteById(1L);
    }

    @Test
    void deposit_ShouldIncreaseBalance() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Account result = accountService.deposit(1L, new BigDecimal("500.00"));


        // Assert
        assertNotNull(result);
        assertEquals(0, new BigDecimal("1500.00").compareTo(result.getBalance()));
    }

    @Test
    void withdraw_WhenSufficientFunds_ShouldDecreaseBalance() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Account result = accountService.withdraw(1L, new BigDecimal("500.00"));

        // Assert
        assertNotNull(result);
        assertEquals(0, new BigDecimal("500.00").compareTo(result.getBalance()));
    }

    @Test
    void withdraw_WhenInsufficientFunds_ShouldThrowException() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        // Act & Assert
        assertThrows(InsufficientFundsException.class, 
            () -> accountService.withdraw(1L, new BigDecimal("1500.00")));
    }

    @Test
    void transfer_WhenSufficientFunds_ShouldTransferAmount() {
        // Arrange
        Account targetAccount = new Account();
        targetAccount.setId(2L);
        targetAccount.setBalance(new BigDecimal("500.00"));
        targetAccount.setCurrencyCode(USD);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(targetAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        accountService.transfer(1L, 2L, new BigDecimal("300.00"));

        // Assert
        assertEquals(new BigDecimal("700.00"), testAccount.getBalance());
        assertEquals(new BigDecimal("800.00"), targetAccount.getBalance());

    }
}

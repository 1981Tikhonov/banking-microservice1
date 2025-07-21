package com.bank.project.repository;

import com.bank.project.entity.Account;
import com.bank.project.entity.AccountStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private Account account;

    @BeforeEach
    public void setUp() {
        // Создаем новый объект Account для теста
        account = new Account();
        account.setClientId(11L);  // Используем существующий client_id, например, 11
        account.setName("Test Account");
        account.setType("Savings");
        account.setStatus(AccountStatus.valueOf("Active"));
        account.setBalance(BigDecimal.valueOf(1000.00));
        account.setCurrencyCode(Integer.valueOf("USD"));
    }

    @Test
    public void testCreateAccount() {
        // Сохраняем аккаунт
        Account savedAccount = accountRepository.save(account);

        // Проверяем, что аккаунт был сохранен
        assertNotNull(savedAccount);
        assertNotNull(savedAccount.getId());
        assertEquals(account.getClientId(), savedAccount.getClientId());
        assertEquals(account.getName(), savedAccount.getName());
    }

    @Test
    public void testFindAccountById() {
        // Сохраняем аккаунт
        Account savedAccount = accountRepository.save(account);

        // Ищем аккаунт по ID
        Optional<Account> foundAccount = accountRepository.findById(savedAccount.getId());

        // Проверяем, что аккаунт найден
        assertTrue(foundAccount.isPresent());
        assertEquals(savedAccount.getId(), foundAccount.get().getId());
    }

    @Test
    public void testUpdateAccountBalance() {
        // Сохраняем аккаунт
        Account savedAccount = accountRepository.save(account);

        // Обновляем баланс
        savedAccount.setBalance(BigDecimal.valueOf(2000.00));
        Account updatedAccount = accountRepository.save(savedAccount);

        // Проверяем, что баланс обновлен
        assertEquals(BigDecimal.valueOf(2000.00), updatedAccount.getBalance());
    }

    @Test
    public void testDeleteAccount() {
        // Сохраняем аккаунт
        Account savedAccount = accountRepository.save(account);

        // Удаляем аккаунт
        accountRepository.delete(savedAccount);

        // Проверяем, что аккаунт удален
        Optional<Account> deletedAccount = accountRepository.findById(savedAccount.getId());
        assertFalse(deletedAccount.isPresent());
    }
}

package com.bank.project.service;

import com.bank.project.entity.Account;
import com.bank.project.entity.AccountStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AccountServiceInterface {
    Account createAccount(Account account);
    Optional<Account> getAccountById(Long id);
    List<Account> getAllAccounts();
    List<Account> getAccountsByClientId(Long clientId);
    List<Account> getAccountsByStatus(String status);
    List<Account> getAccountsByBalanceLessThan(BigDecimal balance);
    List<Account> getAccountsByCurrencyCode(String currencyCode);
    List<Account> getAccountsByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Account> getAccountsByUpdatedAtAfter(LocalDateTime updatedAt);
    Optional<Account> getAccountByName(String name);
    Account updateAccount(Long id, Account account);
    boolean deleteAccount(Long id);
    void performTestOperation();
}

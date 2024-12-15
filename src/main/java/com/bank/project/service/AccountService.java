package com.bank.project.service;

import com.bank.project.entity.Account;
import com.bank.project.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        logger.info("Creating a new account: {}", account);
        Account createdAccount = accountRepository.save(account);
        logger.info("Account created successfully: {}", createdAccount);
        return createdAccount;
    }

    public Optional<Account> getAccountById(Long id) {
        logger.info("Fetching account with ID: {}", id);
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()) {
            logger.info("Account found: {}", account.get());
        } else {
            logger.warn("Account with ID {} not found", id);
        }
        return account;
    }

    public List<Account> getAllAccounts() {
        logger.info("Fetching all accounts");
        List<Account> accounts = accountRepository.findAll();
        logger.info("Total accounts fetched: {}", accounts.size());
        return accounts;
    }

    public List<Account> getAccountsByClientId(Long clientId) {
        logger.info("Fetching accounts for client ID: {}", clientId);
        return accountRepository.findAllByClientId(clientId);
    }

    public List<Account> getAccountsByStatus(String status) {
        logger.info("Fetching accounts with status: {}", status);
        return accountRepository.findAllByStatus(status);
    }

    public List<Account> getAccountsByBalanceLessThan(BigDecimal balance) {
        logger.info("Fetching accounts with balance less than: {}", balance);
        return accountRepository.findAllByBalanceLessThan(balance);
    }

    public List<Account> getAccountsByCurrencyCode(String currencyCode) {
        logger.info("Fetching accounts with currency code: {}", currencyCode);
        return accountRepository.findAllByCurrencyCode(currencyCode);
    }

    public List<Account> getAccountsByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Fetching accounts created between {} and {}", startDate, endDate);
        return accountRepository.findAllByCreatedAtBetween(startDate, endDate);
    }

    public List<Account> getAccountsByUpdatedAtAfter(LocalDateTime updatedAt) {
        logger.info("Fetching accounts updated after: {}", updatedAt);
        return accountRepository.findAllByUpdatedAtAfter(updatedAt);
    }

    public Optional<Account> getAccountByName(String name) {
        logger.info("Fetching account with name: {}", name);
        return accountRepository.findByName(name);
    }

    public Account updateAccount(Long id, Account account) {
        logger.info("Updating account with ID: {}", id);
        if (accountRepository.existsById(id)) {
            account.setId(id);
            Account updatedAccount = accountRepository.save(account);
            logger.info("Account updated successfully: {}", updatedAccount);
            return updatedAccount;
        } else {
            logger.warn("Account with ID {} not found for update", id);
            return null;
        }
    }

    public boolean deleteAccount(Long id) {
        logger.info("Deleting account with ID: {}", id);
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
            logger.info("Account with ID {} deleted successfully", id);
            return true;
        } else {
            logger.warn("Account with ID {} not found for deletion", id);
            return false;
        }
    }
}

package com.bank.project.service;

import com.bank.project.entity.Transaction;
import com.bank.project.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;

    // Creating a transaction
    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        if (transaction.getAmount() <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than 0.");
        }
        transaction.setCreatedAt(null); // Transaction timestamp is set automatically
        Transaction savedTransaction = transactionRepository.save(transaction);

        logger.info("Transaction {} created: debit account {}, credit account {}, amount {}",
                savedTransaction.getId(), transaction.getDebitAccountId(),
                transaction.getCreditAccountId(), transaction.getAmount());
        return savedTransaction;
    }

    // Getting a transaction by ID
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + id));
    }

    // Getting all transactions
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // Getting transactions by debit account ID
    public List<Transaction> getTransactionsByDebitAccountId(Long debitAccountId) {
        return transactionRepository.findByDebitAccountId(debitAccountId);
    }

    // Getting transactions by credit account ID
    public List<Transaction> getTransactionsByCreditAccountId(Long creditAccountId) {
        return transactionRepository.findByCreditAccountId(creditAccountId);
    }

    // Getting transactions by type
    public List<Transaction> getTransactionsByType(String type) {
        return transactionRepository.findByType(type);
    }

    // Getting transactions by amount
    public List<Transaction> getTransactionsByAmount(Double amount) {
        return transactionRepository.findByAmount(amount);
    }

    // Getting transactions by date range
    public List<Transaction> getTransactionsByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByCreatedAtBetween(startDate, endDate);
    }

    // Deleting a transaction
    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = getTransactionById(id);
        transactionRepository.delete(transaction);
        logger.info("Transaction {} deleted.", id);
    }
}

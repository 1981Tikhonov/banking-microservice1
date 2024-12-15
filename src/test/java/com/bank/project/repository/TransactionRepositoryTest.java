package com.bank.project.repository;

import com.bank.project.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    private Transaction transaction1;

    @BeforeEach
    void setUp() {
        // Создаем примеры транзакций для тестирования
        transaction1 = new Transaction();
        transaction1.setDebitAccountId(1L);
        transaction1.setCreditAccountId(2L);
        transaction1.setType("transfer");
        transaction1.setAmount(100.0);
        transaction1.setCreatedAt(LocalDateTime.now().minusDays(1));

        Transaction transaction2 = new Transaction();
        transaction2.setDebitAccountId(2L);
        transaction2.setCreditAccountId(3L);
        transaction2.setType("deposit");
        transaction2.setAmount(200.0);
        transaction2.setCreatedAt(LocalDateTime.now());

        // Сохраняем данные в базу для тестирования
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
    }

    @Test
    void testFindById() {
        // Проверка метода поиска транзакции по ID
        Optional<Transaction> transactionOptional = transactionRepository.findById(transaction1.getId());
        assertThat(transactionOptional).isPresent();
        assertThat(transactionOptional.get().getId()).isEqualTo(transaction1.getId());
    }

    @Test
    void testFindByDebitAccountId() {
        // Проверка метода поиска транзакции по дебетовому счету
        List<Transaction> transactions = transactionRepository.findByDebitAccountId(1L);
        assertThat(transactions).isNotEmpty();
        assertThat(transactions.get(0).getDebitAccountId()).isEqualTo(1L);
    }

    @Test
    void testFindByCreditAccountId() {
        // Проверка метода поиска транзакции по кредитовому счету
        List<Transaction> transactions = transactionRepository.findByCreditAccountId(2L);
        assertThat(transactions).isNotEmpty();
        assertThat(transactions.get(0).getCreditAccountId()).isEqualTo(2L);
    }

    @Test
    void testFindByType() {
        // Проверка метода поиска транзакции по типу
        List<Transaction> transactions = transactionRepository.findByType("transfer");
        assertThat(transactions).isNotEmpty();
        assertThat(transactions.get(0).getType()).isEqualTo("transfer");
    }

    @Test
    void testFindByAmount() {
        // Проверка метода поиска транзакции по сумме
        List<Transaction> transactions = transactionRepository.findByAmount(100.0);
        assertThat(transactions).isNotEmpty();
        assertThat(transactions.get(0).getAmount()).isEqualTo(100.0);
    }

    @Test
    void testFindByCreatedAtBetween() {
        // Проверка метода поиска транзакции по диапазону дат
        LocalDateTime startDate = LocalDateTime.now().minusDays(2);
        LocalDateTime endDate = LocalDateTime.now();
        List<Transaction> transactions = transactionRepository.findByCreatedAtBetween(startDate, endDate);
        assertThat(transactions).hasSize(2);
    }
}

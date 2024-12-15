package com.bank.project.repository;

import com.bank.project.entity.Agreement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AgreementRepositoryTest {

    @Autowired
    private AgreementRepository agreementRepository;

    @BeforeEach
    void setUp() {
        // Создаем примеры данных для тестирования
        Agreement agreement1 = new Agreement();
        agreement1.setAccountId(1L);
        agreement1.setProductId(1L);
        agreement1.setStatus("ACTIVE");
        agreement1.setInterestRate(5.0);
        agreement1.setSum(1000.0);
        agreement1.setCreatedAt(LocalDateTime.now());
        agreement1.setUpdatedAt(LocalDateTime.now());

        Agreement agreement2 = new Agreement();
        agreement2.setAccountId(2L);
        agreement2.setProductId(2L);
        agreement2.setStatus("CLOSED");
        agreement2.setInterestRate(7.0);
        agreement2.setSum(2000.0);
        agreement2.setCreatedAt(LocalDateTime.now());
        agreement2.setUpdatedAt(LocalDateTime.now());

        // Сохраняем данные в базу для тестирования
        agreementRepository.save(agreement1);
        agreementRepository.save(agreement2);
    }

    @Test
    void testFindByAccountId() {
        // Проверка метода поиска по accountId
        List<Agreement> agreements = agreementRepository.findByAccountId(1L);
        assertThat(agreements).hasSize(1);
        assertThat(agreements.get(0).getAccountId()).isEqualTo(1L);
    }

    @Test
    void testFindByProductId() {
        // Проверка метода поиска по productId
        List<Agreement> agreements = agreementRepository.findByProductId(2L);
        assertThat(agreements).hasSize(1);
        assertThat(agreements.get(0).getProductId()).isEqualTo(2L);
    }

    @Test
    void testFindByStatus() {
        // Проверка метода поиска по статусу
        List<Agreement> agreements = agreementRepository.findByStatus("ACTIVE");
        assertThat(agreements).hasSize(1);
        assertThat(agreements.get(0).getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void testFindByInterestRateGreaterThan() {
        // Проверка метода поиска по процентной ставке
        List<Agreement> agreements = agreementRepository.findByInterestRateGreaterThan(6.0);
        assertThat(agreements).hasSize(1);
        assertThat(agreements.get(0).getInterestRate()).isGreaterThan(6.0);
    }

    @Test
    void testFindBySumLessThan() {
        // Проверка метода поиска по сумме
        List<Agreement> agreements = agreementRepository.findBySumLessThan(1500.0);
        assertThat(agreements).hasSize(1);
        assertThat(agreements.get(0).getSum()).isLessThan(1500.0);
    }

    @Test
    void testFindByCreatedAtBetween() {
        // Проверка метода поиска по дате создания
        LocalDateTime now = LocalDateTime.now();
        List<Agreement> agreements = agreementRepository.findByCreatedAtBetween(now.minusDays(1), now.plusDays(1));
        assertThat(agreements).hasSize(2);
    }

    @Test
    void testFindByUpdatedAtAfter() {
        // Проверка метода поиска по дате обновления
        LocalDateTime now = LocalDateTime.now();
        List<Agreement> agreements = agreementRepository.findByUpdatedAtAfter(now.minusDays(1));
        assertThat(agreements).hasSize(2);
    }
}

package com.bank.project.repository;

import com.bank.project.entity.TaxCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaxCodeRepositoryTest {

    @Autowired
    private TaxCodeRepository taxCodeRepository;

    private TaxCode taxCode1;

    @BeforeEach
    void setUp() {
        // Создаем примеры налоговых кодов для тестирования
        taxCode1 = new TaxCode();
        taxCode1.setCode("12345");
        taxCode1.setName("TaxCode1");

        TaxCode taxCode2 = new TaxCode();
        taxCode2.setCode("67890");
        taxCode2.setName("TaxCode2");

        // Сохраняем данные в базу для тестирования
        taxCodeRepository.save(taxCode1);
        taxCodeRepository.save(taxCode2);
    }

    @Test
    void testFindById() {
        // Проверка метода поиска налогового кода по ID
        Optional<TaxCode> taxCodeOptional = taxCodeRepository.findById(taxCode1.getId());
        assertThat(taxCodeOptional).isPresent();
        assertThat(taxCodeOptional.get().getId()).isEqualTo(taxCode1.getId());
    }

    @Test
    void testFindByCode() {
        // Проверка метода поиска налогового кода по коду
        Optional<TaxCode> taxCodeOptional = taxCodeRepository.findByCode("12345");
        assertThat(taxCodeOptional).isPresent();
        assertThat(taxCodeOptional.get().getCode()).isEqualTo("12345");
    }

    @Test
    void testFindByName() {
        // Проверка метода поиска налогового кода по имени
        Optional<TaxCode> taxCodeOptional = taxCodeRepository.findByName("TaxCode2");
        assertThat(taxCodeOptional).isPresent();
        assertThat(taxCodeOptional.get().getName()).isEqualTo("TaxCode2");
    }
}

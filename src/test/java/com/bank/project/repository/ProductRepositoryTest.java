package com.bank.project.repository;

import com.bank.project.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product product1;

    @BeforeEach
    void setUp() {
        // Создаем примеры продуктов для тестирования
        product1 = new Product();
        product1.setName("Product1");
        product1.setStatus("active");
        product1.setCurrencyCode("USD");
        product1.setInterestRate(5.0);
        product1.setCreditLimit(1000.0);

        Product product2 = new Product();
        product2.setName("Product2");
        product2.setStatus("inactive");
        product2.setCurrencyCode("EUR");
        product2.setInterestRate(3.0);
        product2.setCreditLimit(500.0);

        // Сохраняем данные в базу для тестирования
        productRepository.save(product1);
        productRepository.save(product2);
    }

    @Test
    void testFindById() {
        // Проверка метода поиска продукта по ID
        Optional<Product> productOptional = productRepository.findById(product1.getId());
        assertThat(productOptional).isPresent();
        assertThat(productOptional.get().getId()).isEqualTo(product1.getId());
    }

    @Test
    void testFindByName() {
        // Проверка метода поиска продукта по имени
        Optional<Product> productOptional = productRepository.findByName("Product1");
        assertThat(productOptional).isPresent();
        assertThat(productOptional.get().getName()).isEqualTo("Product1");
    }

    @Test
    void testFindByStatus() {
        // Проверка метода поиска продуктов по статусу
        List<Product> activeProducts = productRepository.findByStatus("active");
        assertThat(activeProducts).hasSize(1);
        assertThat(activeProducts.get(0).getStatus()).isEqualTo("active");
    }

    @Test
    void testFindByCurrencyCode() {
        // Проверка метода поиска продуктов по коду валюты
        List<Product> usdProducts = productRepository.findByCurrencyCode("USD");
        assertThat(usdProducts).hasSize(1);
        assertThat(usdProducts.get(0).getCurrencyCode()).isEqualTo("USD");
    }

    @Test
    void testFindByInterestRate() {
        // Проверка метода поиска продуктов по процентной ставке
        List<Product> productsWithInterestRate = productRepository.findByInterestRate(5.0);
        assertThat(productsWithInterestRate).hasSize(1);
        assertThat(productsWithInterestRate.get(0).getInterestRate()).isEqualTo(5.0);
    }

    @Test
    void testFindByCreditLimit() {
        // Проверка метода поиска продуктов по кредитному лимиту
        List<Product> productsWithCreditLimit = productRepository.findByCreditLimit(1000.0);
        assertThat(productsWithCreditLimit).hasSize(1);
        assertThat(productsWithCreditLimit.get(0).getCreditLimit()).isEqualTo(1000.0);
    }

    @Test
    void testFindByStatusAndCurrencyCode() {
        // Проверка метода поиска продуктов по статусу и коду валюты
        List<Product> activeUsdProducts = productRepository.findByStatusAndCurrencyCode("active", "USD");
        assertThat(activeUsdProducts).hasSize(1);
        assertThat(activeUsdProducts.get(0).getStatus()).isEqualTo("active");
        assertThat(activeUsdProducts.get(0).getCurrencyCode()).isEqualTo("USD");
    }

    @Test
    void testFindByStatusAndInterestRate() {
        // Проверка метода поиска продуктов по статусу и процентной ставке
        List<Product> activeInterestRateProducts = productRepository.findByStatusAndInterestRate("active", 5.0);
        assertThat(activeInterestRateProducts).hasSize(1);
        assertThat(activeInterestRateProducts.get(0).getStatus()).isEqualTo("active");
        assertThat(activeInterestRateProducts.get(0).getInterestRate()).isEqualTo(5.0);
    }
}

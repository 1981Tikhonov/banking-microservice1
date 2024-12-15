package com.bank.project.repository;

import com.bank.project.entity.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @NotNull Optional<Product> findById(@NotNull Long id);

    Optional<Product> findByName(String name);  // Добавленный метод для поиска по имени

    List<Product> findByStatus(String status);

    List<Product> findByCurrencyCode(String currencyCode);

    List<Product> findByInterestRate(Double interestRate);

    List<Product> findByCreditLimit(Double creditLimit);

    List<Product> findByStatusAndCurrencyCode(String status, String currencyCode);

    List<Product> findByStatusAndInterestRate(String status, Double interestRate);
}

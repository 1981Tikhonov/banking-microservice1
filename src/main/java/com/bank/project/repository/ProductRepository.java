package com.bank.project.repository;

import com.bank.project.entity.Product;
import com.bank.project.entity.enums.ProductStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @NotNull 
    Optional<Product> findById(@NotNull Long id);

    Optional<Product> findByName(String name);
    
    List<Product> findByDescriptionContainingIgnoreCase(String description);

    List<Product> findByStatus(ProductStatus status);

    List<Product> findByCurrencyCode(String currencyCode);

    List<Product> findByInterestRate(BigDecimal interestRate);

    List<Product> findByCreditLimit(BigDecimal creditLimit);
    
    List<Product> findByCreditLimitLessThanEqual(BigDecimal maxCreditLimit);
    
    List<Product> findByMinAmountGreaterThanEqual(BigDecimal minAmount);
    
    List<Product> findByMaxAmountLessThanEqual(BigDecimal maxAmount);
    
    List<Product> findByIsActiveTrue();
    
    List<Product> findByIsActive(Boolean isActive);
    
    List<Product> findByStatusAndCurrencyCode(ProductStatus status, String currencyCode);
    
    List<Product> findByStatusAndInterestRate(ProductStatus status, BigDecimal interestRate);
    
    List<Product> findByManagerId(Long managerId);
}

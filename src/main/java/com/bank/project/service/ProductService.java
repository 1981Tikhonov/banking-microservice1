package com.bank.project.service;

import com.bank.project.entity.Product;
import com.bank.project.entity.enums.ProductStatus;
import com.bank.project.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        logger.info("Creating new product with name: {}", product.getName());
        // createdAt and updatedAt will be set by @PrePersist
        Product savedProduct = productRepository.save(product);
        logger.info("Product created successfully with ID: {}", savedProduct.getId());
        return savedProduct;
    }

    public Product getProductById(Long id) {
        logger.info("Fetching product with ID: {}", id);
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() -> {
            logger.error("Product not found with ID: {}", id);
            return new RuntimeException("Product not found with ID: " + id);
        });
    }

    public List<Product> getAllProducts() {
        logger.debug("Fetching all products from the database");
        return productRepository.findAll();
    }

    public Product updateProduct(Long id, Product productDetails) {
        logger.info("Updating product with ID: {}", id);
        Product existingProduct = getProductById(id);
        
        // Update only non-null fields from productDetails
        if (productDetails.getName() != null) {
            existingProduct.setName(productDetails.getName());
        }
        if (productDetails.getDescription() != null) {
            existingProduct.setDescription(productDetails.getDescription());
        }
        if (productDetails.getStatus() != null) {
            existingProduct.setStatus(productDetails.getStatus());
        }
        if (productDetails.getCurrencyCode() != null) {
            existingProduct.setCurrencyCode(productDetails.getCurrencyCode());
        }
        if (productDetails.getInterestRate() != null) {
            existingProduct.setInterestRate(productDetails.getInterestRate());
        }
        if (productDetails.getCreditLimit() != null) {
            existingProduct.setCreditLimit(productDetails.getCreditLimit());
        }
        if (productDetails.getMinAmount() != null) {
            existingProduct.setMinAmount(productDetails.getMinAmount());
        }
        if (productDetails.getMaxAmount() != null) {
            existingProduct.setMaxAmount(productDetails.getMaxAmount());
        }
        if (productDetails.getIsActive() != null) {
            existingProduct.setIsActive(productDetails.getIsActive());
        }
        
        // updatedAt will be set by @PreUpdate
        Product updatedProduct = productRepository.save(existingProduct);
        logger.info("Product with ID: {} updated successfully", id);
        return updatedProduct;
    }

    public boolean deleteProduct(Long id) {
        logger.info("Deleting product with ID: {}", id);
        Product product = getProductById(id);
        productRepository.delete(product);
        logger.info("Product with ID: {} deleted successfully", id);
        return false;
    }

    public Product findProductByName(String name) {
        logger.info("Searching for product with name: {}", name);
        Optional<Product> product = productRepository.findByName(name);
        return product.orElseThrow(() -> {
            logger.error("Product not found with name: {}", name);
            return new RuntimeException("Product not found with name: " + name);
        });
    }

    public List<Product> findProductsByStatus(String status) {
        logger.info("Finding products with status: {}", status);
        return productRepository.findByStatus(ProductStatus.valueOf(status));
    }

    public List<Product> findProductsByCurrencyCode(String currencyCode) {
        logger.info("Finding products with currency code: {}", currencyCode);
        return productRepository.findByCurrencyCode(currencyCode);
    }

    public List<Product> findProductsByInterestRate(BigDecimal interestRate) {
        logger.info("Finding products with interest rate: {}", interestRate);
        return productRepository.findByInterestRate(interestRate);
    }

    public List<Product> findProductsByCreditLimit(BigDecimal creditLimit) {
        logger.info("Finding products with credit limit: {}", creditLimit);
        return productRepository.findByCreditLimit(creditLimit);
    }
    
    public List<Product> findActiveProducts() {
        logger.info("Finding all active products");
        return productRepository.findByIsActiveTrue();
    }
    
    public List<Product> findProductsByMinAmount(BigDecimal minAmount) {
        logger.info("Finding products with minimum amount: {}", minAmount);
        return productRepository.findByMinAmountGreaterThanEqual(minAmount);
    }

    public List<Product> findProductsByStatusAndCurrencyCode(String status, String currencyCode) {
        logger.info("Finding products with status: {} and currency code: {}", status, currencyCode);
        return productRepository.findByStatusAndCurrencyCode(ProductStatus.valueOf(status), currencyCode);
    }

    public List<Product> findProductsByStatusAndInterestRate(String status, Double interestRate) {
        logger.info("Finding products with status: {} and interest rate: {}", status, interestRate);
        return productRepository.findByStatusAndInterestRate(ProductStatus.valueOf(status), BigDecimal.valueOf(interestRate));
    }
}

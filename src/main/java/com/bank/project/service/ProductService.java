package com.bank.project.service;

import com.bank.project.entity.Product;
import com.bank.project.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
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
        existingProduct.setName(productDetails.getName());
        existingProduct.setStatus(productDetails.getStatus());
        existingProduct.setCurrencyCode(productDetails.getCurrencyCode());
        existingProduct.setInterestRate(productDetails.getInterestRate());
        existingProduct.setCreditLimit(productDetails.getCreditLimit());
        existingProduct.setUpdatedAt(LocalDateTime.now());
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
        return productRepository.findByStatus(status);
    }

    public List<Product> findProductsByCurrencyCode(String currencyCode) {
        logger.info("Finding products with currency code: {}", currencyCode);
        return productRepository.findByCurrencyCode(currencyCode);
    }

    public List<Product> findProductsByInterestRate(Double interestRate) {
        logger.info("Finding products with interest rate: {}", interestRate);
        return productRepository.findByInterestRate(interestRate);
    }

    public List<Product> findProductsByCreditLimit(Double creditLimit) {
        logger.info("Finding products with credit limit: {}", creditLimit);
        return productRepository.findByCreditLimit(creditLimit);
    }

    public List<Product> findProductsByStatusAndCurrencyCode(String status, String currencyCode) {
        logger.info("Finding products with status: {} and currency code: {}", status, currencyCode);
        return productRepository.findByStatusAndCurrencyCode(status, currencyCode);
    }

    public List<Product> findProductsByStatusAndInterestRate(String status, Double interestRate) {
        logger.info("Finding products with status: {} and interest rate: {}", status, interestRate);
        return productRepository.findByStatusAndInterestRate(status, interestRate);
    }
}

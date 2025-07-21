package com.bank.project.controller;

import com.bank.project.entity.Product;
import com.bank.project.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Create a new product", description = "Create a new product and return the created product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product data")
    })
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        logger.info("Request to create a new product: {}", product);
        Product createdProduct = productService.createProduct(product);
        logger.info("Product created successfully with ID: {}", createdProduct.getId());
        return ResponseEntity.ok(createdProduct);
    }

    @Operation(summary = "Get a product by ID", description = "Fetch a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "ID of the product to be fetched") @PathVariable Long id) {
        logger.info("Request to fetch product with ID: {}", id);
        Product product = productService.getProductById(id);
        if (product != null) {
            logger.info("Product found: {}", product);
            return ResponseEntity.ok(product);
        } else {
            logger.warn("Product with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get all products", description = "Fetch a list of all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products fetched successfully")
    })
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        logger.info("Request to fetch all products");
        List<Product> products = productService.getAllProducts();
        logger.info("Total products fetched: {}", products.size());
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Update a product", description = "Update the details of a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "ID of the product to be updated") @PathVariable Long id,
            @RequestBody Product product) {
        logger.info("Request to update product with ID: {}", id);
        Product updatedProduct = productService.updateProduct(id, product);
        if (updatedProduct != null) {
            logger.info("Product updated successfully: {}", updatedProduct);
            return ResponseEntity.ok(updatedProduct);
        } else {
            logger.warn("Product with ID: {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a product", description = "Delete a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        logger.info("Request to delete product with ID: {}", id);
        boolean isDeleted = productService.deleteProduct(id);
        if (isDeleted) {
            logger.info("Product with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Product with ID: {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get products by status", description = "Fetch products by their status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products fetched successfully")
    })
    @GetMapping("/filter/status/{status}")
    public ResponseEntity<List<Product>> getProductsByStatus(@PathVariable String status) {
        logger.info("Request to fetch products with status: {}", status);
        List<Product> products = productService.findProductsByStatus(status);
        logger.info("Products found with status {}: {}", status, products.size());
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get products by currency code", description = "Fetch products by their currency code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products fetched successfully")
    })
    @GetMapping("/filter/currency/{currencyCode}")
    public ResponseEntity<List<Product>> getProductsByCurrencyCode(@PathVariable String currencyCode) {
        logger.info("Request to fetch products with currency code: {}", currencyCode);
        List<Product> products = productService.findProductsByCurrencyCode(currencyCode);
        logger.info("Products found with currency code {}: {}", currencyCode, products.size());
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get products by interest rate", description = "Fetch products by their interest rate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products fetched successfully")
    })
    @GetMapping("/filter/interestRate/{interestRate}")
    public ResponseEntity<List<Product>> getProductsByInterestRate(@PathVariable Double interestRate) {
        logger.info("Request to fetch products with interest rate: {}", interestRate);
        List<Product> products = productService.findProductsByInterestRate(BigDecimal.valueOf(interestRate));
        logger.info("Products found with interest rate {}: {}", interestRate, products.size());
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get products by credit limit", description = "Fetch products by their credit limit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products fetched successfully")
    })
    @GetMapping("/filter/creditLimit/{creditLimit}")
    public ResponseEntity<List<Product>> getProductsByCreditLimit(@PathVariable Double creditLimit) {
        logger.info("Request to fetch products with credit limit: {}", creditLimit);
        List<Product> products = productService.findProductsByCreditLimit(BigDecimal.valueOf(creditLimit));
        logger.info("Products found with credit limit {}: {}", creditLimit, products.size());
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get products by status and currency code", description = "Fetch products by their status and currency code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products fetched successfully")
    })
    @GetMapping("/filter/status/{status}/currency/{currencyCode}")
    public ResponseEntity<List<Product>> getProductsByStatusAndCurrencyCode(
            @PathVariable String status,
            @PathVariable String currencyCode) {
        logger.info("Request to fetch products with status: {} and currency code: {}", status, currencyCode);
        List<Product> products = productService.findProductsByStatusAndCurrencyCode(status, currencyCode);
        logger.info("Products found with status {} and currency code {}: {}", status, currencyCode, products.size());
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get products by status and interest rate", description = "Fetch products by their status and interest rate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products fetched successfully")
    })
    @GetMapping("/filter/status/{status}/interestRate/{interestRate}")
    public ResponseEntity<List<Product>> getProductsByStatusAndInterestRate(
            @PathVariable String status,
            @PathVariable Double interestRate) {
        logger.info("Request to fetch products with status: {} and interest rate: {}", status, interestRate);
        List<Product> products = productService.findProductsByStatusAndInterestRate(status, interestRate);
        logger.info("Products found with status {} and interest rate {}: {}", status, interestRate, products.size());
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get a product by name", description = "Fetch a product by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name) {
        logger.info("Request to fetch product with name: {}", name);
        Product product = productService.findProductByName(name);
        if (product != null) {
            logger.info("Product found: {}", product);
            return ResponseEntity.ok(product);
        } else {
            logger.warn("Product with name: {} not found", name);
            return ResponseEntity.notFound().build();
        }
    }
}

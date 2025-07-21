package com.bank.project.controller.integration;

import com.bank.project.entity.Product;
import com.bank.project.entity.enums.ProductStatus;
import com.bank.project.entity.enums.ProductType;
import com.bank.project.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.bank.project.entity.enums.ProductType.LOAN;
import static com.bank.project.entity.enums.ProductType.SAVINGS;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    private Product testProduct;

    @BeforeEach
    void setUp() throws InterruptedException {
        // Clean up before each test
        productRepository.deleteAll();

        // Create test product
        testProduct = new Product();
        testProduct.setName("Premium Savings");
        testProduct.setType(SAVINGS);
        testProduct.setStatus(ProductStatus.ACTIVE);
        testProduct.setCurrency("USD");
        testProduct.setInterestRate(new BigDecimal("2.5"));
        testProduct.setMinDeposit(new BigDecimal("100.00"));
        testProduct.setMaxDeposit(new BigDecimal("100000.00"));
        testProduct.setTermMonths(12);
        testProduct = productRepository.save(testProduct);
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        String productJson = """
            {
                "name": "Student Account",
                "type": "CHECKING",
                "status": "ACTIVE",
                "currency": "USD",
                "interestRate": 0.5,
                "minDeposit": 0.00,
                "maxDeposit": 10000.00,
                "termMonths": 0,
                "description": "Student account with no fees"
            }
            """;

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Student Account"))
                .andExpect(jsonPath("$.type").value("CHECKING"))
                .andExpect(jsonPath("$.interestRate").value(0.5));
    }

    @Test
    void getProductById_ShouldReturnProduct() throws Exception {
        mockMvc.perform(get("/api/products/{id}", testProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testProduct.getId()))
                .andExpect(jsonPath("$.name").value("Premium Savings"))
                .andExpect(jsonPath("$.type").value("SAVINGS"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void updateProduct_ShouldUpdateProduct() throws Exception {
        String updateJson = """
            {
                "name": "Premium Savings Plus",
                "interestRate": 3.0,
                "status": "INACTIVE"
            }
            """;

        mockMvc.perform(put("/api/products/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Premium Savings Plus"))
                .andExpect(jsonPath("$.interestRate").value(3.0))
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/products/{id}", testProduct.getId()))
                .andExpect(status().isNoContent());

        // Verify product is deleted
        mockMvc.perform(get("/api/products/{id}", testProduct.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllProducts_ShouldReturnPageOfProducts() throws Exception {
        // Add more test products
        for (int i = 0; i < 5; i++) {
            Product product = new Product();
            product.setName("Product " + i);
            product.setType(ProductType.SAVINGS);
            product.setStatus(ProductStatus.ACTIVE);
            product.setCurrency("USD");
            product.setInterestRate(new BigDecimal("1.5"));
            productRepository.save(product);
        }

        mockMvc.perform(get("/api/products")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.content[*].status", everyItem(is("ACTIVE"))))
                .andExpect(jsonPath("$.content[*].type", everyItem(notNullValue())));
    }

    @Test
    void getProductsByType_ShouldReturnFilteredProducts() throws Exception {
        // Create a loan product
        Product loanProduct = new Product();
        loanProduct.setName("Personal Loan");
        loanProduct.setType(LOAN);
        loanProduct.setStatus(ProductStatus.ACTIVE);
        loanProduct.setCurrency("USD");
        loanProduct.setInterestRate(new BigDecimal("7.5"));
        productRepository.save(loanProduct);

        mockMvc.perform(get("/api/products/type/LOAN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type").value("LOAN"));
    }

    @Test
    void getProductsByStatus_ShouldReturnFilteredProducts() throws Exception {
        // Create an inactive product
        Product inactiveProduct = new Product();
        inactiveProduct.setName("Old Product");
        inactiveProduct.setType(SAVINGS);
        inactiveProduct.setStatus(ProductStatus.INACTIVE);
        inactiveProduct.setCurrency("USD");
        inactiveProduct.setInterestRate(new BigDecimal("1.0"));
        productRepository.save(inactiveProduct);

        mockMvc.perform(get("/api/products/status/INACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status").value("INACTIVE"));
    }

    @Test
    void createProduct_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        String invalidProductJson = """
            {
                "name": "",
                "type": "INVALID_TYPE",
                "interestRate": -1.0
            }
            """;

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidProductJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(greaterThan(0))));
    }
}

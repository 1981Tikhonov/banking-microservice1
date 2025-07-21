package com.bank.project.service;

import com.bank.project.entity.Product;
import com.bank.project.entity.enums.ProductStatus;
import com.bank.project.exception.ResourceNotFoundException;
import com.bank.project.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Premium Savings");
        testProduct.setStatus(ProductStatus.valueOf("ACTIVE"));
        testProduct.setCurrencyCode("USD");
        testProduct.setInterestRate(new BigDecimal("2.5"));
        testProduct.setMinBalance(new BigDecimal("1000"));
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() {
        // Arrange
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product createdProduct = productService.createProduct(testProduct);

        // Assert
        assertNotNull(createdProduct);
        assertEquals("Premium Savings", createdProduct.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // Act
        Product foundProduct = productService.getProductById(1L);

        // Assert
        assertNotNull(foundProduct);
        assertEquals("Premium Savings", foundProduct.getName());
    }

    @Test
    void getProductById_WhenProductNotExists_ShouldThrowException() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());


        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(999L));
    }

    @Test
    void getAllProducts_ShouldReturnPageOfProducts() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<Product> page = new PageImpl<>(List.of(testProduct));
        when(productRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<Product> result = productService.getAllProducts(0, 10, new String[]{"name,asc"});

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Premium Savings", result.getContent().get(0).getName());
    }

    @Test
    void getProductsByStatus_ShouldReturnFilteredProducts() {
        // Arrange
        when(productRepository.findByStatus(ProductStatus.valueOf("ACTIVE"))).thenReturn(List.of(testProduct));


        // Act
        List<Product> products = productService.getProductsByStatus("ACTIVE");

        // Assert
        assertFalse(products.isEmpty());
        assertEquals(ProductStatus.ACTIVE, products.get(0).getStatus());
    }

    @Test
    void updateProduct_WhenProductExists_ShouldUpdateProduct() {
        // Arrange
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Savings");
        updatedProduct.setInterestRate(new BigDecimal("3.0"));

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Product result = productService.updateProduct(1L, updatedProduct);


        // Assert
        assertNotNull(result);
        assertEquals("Updated Savings", result.getName());
        assertEquals(0, new BigDecimal("3.0").compareTo(result.getInterestRate()));
        assertEquals(ProductStatus.ACTIVE, result.getStatus()); // Статус не должен измениться
    }

    @Test
    void updateProduct_WhenProductNotExists_ShouldThrowException() {
        // Arrange
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Savings");

        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> productService.updateProduct(999L, updatedProduct));
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldDeleteProduct() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        // Act
        boolean result = productService.deleteProduct(1L);

        // Assert
        assertTrue(result);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProduct_WhenProductNotExists_ShouldReturnFalse() {
        // Arrange
        when(productRepository.existsById(999L)).thenReturn(false);


        // Act
        boolean result = productService.deleteProduct(999L);


        // Assert
        assertFalse(result);
        verify(productRepository, never()).deleteById(anyLong());
    }
}

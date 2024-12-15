package com.bank.project.service;

import com.bank.project.entity.Product;
import com.bank.project.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Создание mock продукта для тестов
        product = new Product();
        product.setId(1L);
        product.setName("Product A");
        product.setStatus("ACTIVE");
        product.setCurrencyCode("USD");
        product.setInterestRate(5.0);
        product.setCreditLimit(10000.0);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createProduct(product);

        assertNotNull(createdProduct);
        assertEquals("Product A", createdProduct.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(1L);

        assertNotNull(foundProduct);
        assertEquals("Product A", foundProduct.getName());
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProductById(1L));
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals("Product A", products.get(0).getName());
    }

    @Test
    void testUpdateProduct() {
        Product updatedProductDetails = new Product();
        updatedProductDetails.setName("Updated Product");
        updatedProductDetails.setStatus("INACTIVE");
        updatedProductDetails.setCurrencyCode("EUR");
        updatedProductDetails.setInterestRate(6.0);
        updatedProductDetails.setCreditLimit(15000.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = productService.updateProduct(1L, updatedProductDetails);

        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals("INACTIVE", updatedProduct.getStatus());
        assertEquals("EUR", updatedProduct.getCurrencyCode());
        assertEquals(6.0, updatedProduct.getInterestRate());
        assertEquals(15000.0, updatedProduct.getCreditLimit());
        verify(productRepository, times(1)).save(updatedProduct);
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        boolean result = productService.deleteProduct(1L);

        assertTrue(result);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testFindProductByName() {
        when(productRepository.findByName("Product A")).thenReturn(Optional.of(product));

        Product foundProduct = productService.findProductByName("Product A");

        assertNotNull(foundProduct);
        assertEquals("Product A", foundProduct.getName());
    }

    @Test
    void testFindProductByName_NotFound() {
        when(productRepository.findByName("Non-existent Product")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.findProductByName("Non-existent Product"));
    }

    @Test
    void testFindProductsByStatus() {
        when(productRepository.findByStatus("ACTIVE")).thenReturn(List.of(product));

        List<Product> products = productService.findProductsByStatus("ACTIVE");

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals("ACTIVE", products.get(0).getStatus());
    }

    @Test
    void testFindProductsByCurrencyCode() {
        when(productRepository.findByCurrencyCode("USD")).thenReturn(List.of(product));

        List<Product> products = productService.findProductsByCurrencyCode("USD");

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals("USD", products.get(0).getCurrencyCode());
    }

    @Test
    void testFindProductsByInterestRate() {
        when(productRepository.findByInterestRate(5.0)).thenReturn(List.of(product));

        List<Product> products = productService.findProductsByInterestRate(5.0);

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals(5.0, products.get(0).getInterestRate());
    }

    @Test
    void testFindProductsByCreditLimit() {
        when(productRepository.findByCreditLimit(10000.0)).thenReturn(List.of(product));

        List<Product> products = productService.findProductsByCreditLimit(10000.0);

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals(10000.0, products.get(0).getCreditLimit());
    }

    @Test
    void testFindProductsByStatusAndCurrencyCode() {
        when(productRepository.findByStatusAndCurrencyCode("ACTIVE", "USD")).thenReturn(List.of(product));

        List<Product> products = productService.findProductsByStatusAndCurrencyCode("ACTIVE", "USD");

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals("ACTIVE", products.get(0).getStatus());
        assertEquals("USD", products.get(0).getCurrencyCode());
    }

    @Test
    void testFindProductsByStatusAndInterestRate() {
        when(productRepository.findByStatusAndInterestRate("ACTIVE", 5.0)).thenReturn(List.of(product));

        List<Product> products = productService.findProductsByStatusAndInterestRate("ACTIVE", 5.0);

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals("ACTIVE", products.get(0).getStatus());
        assertEquals(5.0, products.get(0).getInterestRate());
    }
}

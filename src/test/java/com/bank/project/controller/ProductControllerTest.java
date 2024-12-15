package com.bank.project.controller;

import com.bank.project.entity.Product;
import com.bank.project.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testCreateProduct() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product A");

        when(productService.createProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType("application/json")
                        .content("{\"name\": \"Product A\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Product A"));

        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    public void testGetProductById() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product A");

        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Product A"));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    public void testGetAllProducts() throws Exception {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product A");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product B");

        List<Product> productList = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Product A"))
                .andExpect(jsonPath("$[1].name").value("Product B"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product A");

        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(product);

        mockMvc.perform(put("/api/products/{id}", 1L)
                        .contentType("application/json")
                        .content("{\"name\": \"Product A\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Product A"));

        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        when(productService.deleteProduct(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/products/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    public void testDeleteProductNotFound() throws Exception {
        when(productService.deleteProduct(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/products/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).deleteProduct(1L);
    }
}

package com.example.producto.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.example.producto.impl.ProductServiceImpl;
import com.example.producto.model.Product;
import com.example.producto.dto.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceImpl service;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("High performance");
        product.setPrice(BigDecimal.valueOf(1200.99));
        product.setSku("ABC12345");
        product.setStock(5);
    }

    @Test
    void shouldReturnAllProducts() throws Exception {
        Mockito.when(service.getAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Laptop")))
                .andExpect(jsonPath("$[0].price", is(1200.99)));
    }

    @Test
    void shouldReturnProductById() throws Exception {
        Mockito.when(service.getById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Laptop")))
                .andExpect(jsonPath("$.stock", is(5)));
    }

    @Test
    void shouldCreateProduct() throws Exception {
        Product created = new Product();
        created.setId(2L);
        created.setName("Mouse");
        created.setDescription("Wireless");
        created.setPrice(BigDecimal.valueOf(50.00));
        created.setSku("MOUSE123");
        created.setStock(10);

        Mockito.when(service.create(Mockito.any(Product.class))).thenReturn(created);

        String body = """
                {
                  "name": "Mouse",
                  "description": "Wireless",
                  "price": 50.00,
                  "sku": "MOUSE123",
                  "stock": 10
                }
                """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/products/2"))
                .andExpect(jsonPath("$.name", is("Mouse")));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        Product updated = new Product();
        updated.setId(1L);
        updated.setName("Laptop Pro");
        updated.setDescription("Upgraded model");
        updated.setPrice(BigDecimal.valueOf(1500.00));
        updated.setSku("LAPTOPPRO");
        updated.setStock(7);

        Mockito.when(service.update(Mockito.eq(1L), Mockito.any(Product.class)))
                .thenReturn(updated);

        String body = """
                {
                  "name": "Laptop Pro",
                  "description": "Upgraded model",
                  "price": 1500.00,
                  "sku": "LAPTOPPRO",
                  "stock": 7
                }
                """;

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Laptop Pro")))
                .andExpect(jsonPath("$.price", is(1500.00)));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }
}
package com.example.producto.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.example.producto.exception.ResourceNotFoundException;
import com.example.producto.model.Product;
import com.example.producto.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository repo;

    @InjectMocks
    private ProductServiceImpl service;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("Laptop gamer");
        product.setPrice(BigDecimal.valueOf(10.0));
        product.setStock(10);
        product.setSku("ABC12345");
    }

    @Test
    void testCreateProduct_WithSku() {
        when(repo.save(any(Product.class))).thenReturn(product);

        Product created = service.create(product);

        assertNotNull(created);
        assertEquals("Laptop", created.getName());
        verify(repo, times(1)).save(any(Product.class));
    }

    @Test
    void testCreateProduct_WithoutSku_GeneratesSku() {
        product.setSku(null);
        when(repo.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            assertNotNull(p.getSku());
            assertTrue(p.getSku().length() == 8);
            return p;
        });

        Product created = service.create(product);

        assertNotNull(created.getSku());
    }

    @Test
    void testUpdateProduct_Success() {
        Product updatedData = new Product();
        updatedData.setName("Mouse");
        updatedData.setDescription("Wireless mouse");
        updatedData.setPrice(BigDecimal.valueOf(10.0));
        updatedData.setStock(20);
        updatedData.setSku("NEW12345");

        when(repo.findById(1L)).thenReturn(Optional.of(product));
        when(repo.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product updated = service.update(1L, updatedData);

        assertEquals("Mouse", updated.getName());
        assertEquals("Wireless mouse", updated.getDescription());
        verify(repo, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.update(99L, product));
        verify(repo, never()).save(any());
    }

    @Test
    void testGetById_Success() {
        when(repo.findById(1L)).thenReturn(Optional.of(product));
        Product found = service.getById(1L);
        assertEquals("Laptop", found.getName());
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void testGetAllProducts() {
        when(repo.findAll()).thenReturn(List.of(product, new Product()));
        List<Product> result = service.getAll();
        assertEquals(2, result.size());
        verify(repo, times(1)).findAll();
    }

    @Test
    void testDeleteProduct_Success() {
        when(repo.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(repo).delete(product);

        service.delete(1L);

        verify(repo, times(1)).delete(product);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(repo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete(2L));
        verify(repo, never()).delete(any());
    }
}
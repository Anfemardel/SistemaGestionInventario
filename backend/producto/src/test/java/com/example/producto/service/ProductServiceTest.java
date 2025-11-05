package com.example.producto.service;

import com.example.producto.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;

import com.example.producto.exception.ResourceNotFoundException;
import com.example.producto.model.Product;
import com.example.producto.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository repo;

    @InjectMocks
    private ProductServiceImpl service;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("Gaming laptop");
        product.setPrice(BigDecimal.valueOf(2000));
        product.setStock(5);
        product.setSku("ABC123");
    }

    // ✅ Test: crear producto
    @Test
    void create_ShouldSaveProduct() {
        when(repo.save(any(Product.class))).thenReturn(product);

        Product result = service.create(product);

        assertThat(result).isNotNull();
        verify(repo, times(1)).save(any(Product.class));
    }

    // ✅ Test: obtener por ID
    @Test
    void getById_ShouldReturnProduct_WhenExists() {
        when(repo.findById(1L)).thenReturn(Optional.of(product));

        Product result = service.getById(1L);

        assertThat(result.getName()).isEqualTo("Laptop");
        verify(repo).findById(1L);
    }

    // 🚨 Test: obtener por ID que no existe
    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    // ✅ Test: listar productos
    @Test
    void getAll_ShouldReturnProductList() {
        when(repo.findAll()).thenReturn(List.of(product));

        List<Product> result = service.getAll();

        assertThat(result).hasSize(1);
        verify(repo).findAll();
    }

    // ✅ Test: actualizar producto existente
    @Test
    void update_ShouldModifyProduct() {
        Product updated = new Product();
        updated.setName("Laptop Pro");
        updated.setDescription("High-end");
        updated.setPrice(BigDecimal.valueOf(2500));
        updated.setStock(10);
        updated.setSku("XYZ789");

        when(repo.findById(1L)).thenReturn(Optional.of(product));
        when(repo.save(any(Product.class))).thenReturn(updated);

        Product result = service.update(1L, updated);

        assertThat(result.getName()).isEqualTo("Laptop Pro");
        verify(repo).save(any(Product.class));
    }

    // 🚨 Test: actualizar producto inexistente
    @Test
    void update_ShouldThrowException_WhenProductNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(99L, product));
    }

    // ✅ Test: eliminar producto
    @Test
    void delete_ShouldRemoveProduct() {
        when(repo.findById(1L)).thenReturn(Optional.of(product));

        service.delete(1L);

        verify(repo).delete(product);
    }

    // 🚨 Test: eliminar producto inexistente
    @Test
    void delete_ShouldThrowException_WhenNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));
    }
}
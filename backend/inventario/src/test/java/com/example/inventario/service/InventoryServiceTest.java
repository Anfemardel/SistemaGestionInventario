package com.example.inventario.service;

import com.example.inventario.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private InventoryService inventoryService;

    private final String productServiceUrl = "http://localhost:8080/api/products";

    @BeforeEach
    void setUp() {
        // Inicializa los mocks antes de cada test
        MockitoAnnotations.openMocks(this);
        // Reinicializa el servicio con URL simulada
        inventoryService = new InventoryService(restTemplate, productServiceUrl);
    }

    @Test
    @DisplayName("Debe devolver el stock actual del producto")
    void shouldReturnStock() {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Laptop");
        product.setStock(15);

        when(restTemplate.getForObject(productServiceUrl + "/" + productId, Product.class))
                .thenReturn(product);

        // Act
        Integer stock = inventoryService.getStock(productId);

        // Assert
        assertEquals(15, stock);
        verify(restTemplate, times(1)).getForObject(productServiceUrl + "/" + productId, Product.class);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el producto no existe al consultar stock")
    void shouldThrowExceptionWhenProductNotFoundOnGetStock() {
        Long productId = 2L;

        when(restTemplate.getForObject(productServiceUrl + "/" + productId, Product.class))
                .thenReturn(null);

        Exception ex = assertThrows(RuntimeException.class,
                () -> inventoryService.getStock(productId));

        assertEquals("Producto no encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("Debe reducir el stock correctamente tras una compra")
    void shouldUpdateStockSuccessfully() {
        // Arrange
        Long productId = 3L;
        Product product = new Product();
        product.setId(productId);
        product.setStock(10);

        when(restTemplate.getForObject(productServiceUrl + "/" + productId, Product.class))
                .thenReturn(product);

        // Act
        inventoryService.updateStock(productId, 3);

        // Assert
        assertEquals(7, product.getStock()); // stock actualizado localmente
        verify(restTemplate, times(1)).put(productServiceUrl + "/" + productId, product);
    }

    @Test
    @DisplayName("Debe dejar el stock en 0 si se intenta comprar más de lo disponible")
    void shouldNotGoBelowZero() {
        // Arrange
        Long productId = 4L;
        Product product = new Product();
        product.setId(productId);
        product.setStock(2);

        when(restTemplate.getForObject(productServiceUrl + "/" + productId, Product.class))
                .thenReturn(product);

        // Act
        inventoryService.updateStock(productId, 5);

        // Assert
        assertEquals(0, product.getStock());
        verify(restTemplate, times(1)).put(productServiceUrl + "/" + productId, product);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el producto no existe al actualizar")
    void shouldThrowExceptionWhenProductNotFoundOnUpdate() {
        Long productId = 99L;

        when(restTemplate.getForObject(productServiceUrl + "/" + productId, Product.class))
                .thenReturn(null);

        Exception ex = assertThrows(RuntimeException.class,
                () -> inventoryService.updateStock(productId, 2));

        assertEquals("Producto no encontrado", ex.getMessage());
        verify(restTemplate, never()).put(anyString(), any());
    }
}
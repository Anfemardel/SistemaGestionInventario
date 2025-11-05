package com.example.inventario.controller;

import org.junit.jupiter.api.Test;
import com.example.inventario.service.InventoryService;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @Test
    @DisplayName("Debe retornar el stock de un producto")
    void shouldReturnStockForProduct() throws Exception {
        // Arrange
        Long productId = 5L;
        when(inventoryService.getStock(productId)).thenReturn(12);

        // Act & Assert
        mockMvc.perform(get("/api/inventories/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(content().string("12"));

        verify(inventoryService, times(1)).getStock(productId);
    }

    @Test
    @DisplayName("Debe actualizar el stock al comprar un producto")
    void shouldUpdateStockOnPurchase() throws Exception {
        // Arrange
        Long productId = 3L;
        int quantity = 4;
        doNothing().when(inventoryService).updateStock(productId, quantity);

        // Act & Assert
        mockMvc.perform(put("/api/inventories/{productId}/purchase", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\": " + quantity + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Inventario actualizado para producto " + productId));

        verify(inventoryService, times(1)).updateStock(productId, quantity);
    }
}
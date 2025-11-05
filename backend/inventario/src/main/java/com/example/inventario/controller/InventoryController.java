package com.example.inventario.controller;

import com.example.inventario.service.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/inventories")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Consultar cantidad de un producto
    @GetMapping("/{productId}")
    public int getInventory(@PathVariable Long productId) {
        return inventoryService.getStock(productId);
    }

    @PutMapping("/{productId}/purchase")
    public Map<String, Object> purchaseProduct(@PathVariable Long productId,
                                               @RequestBody Map<String, Integer> body) {
        int quantity = body.getOrDefault("quantity", 1);
        inventoryService.updateStock(productId, quantity);
        return Map.of("message", "Inventario actualizado para producto " + productId);
    }
}

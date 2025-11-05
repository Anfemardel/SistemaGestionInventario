package com.example.inventario.service;

import com.example.inventario.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InventoryService {
    private final RestTemplate restTemplate;
    private final String productServiceUrl;

    // Constructor con inyección de dependencias
    public InventoryService(RestTemplate restTemplate,
                            @Value("${services.products.url}") String productServiceUrl) {
        this.restTemplate = restTemplate;
        this.productServiceUrl = productServiceUrl;
    }

    // Consultar stock
    public Integer getStock(Long productId) {
        String url = productServiceUrl + "/" + productId;
        Product product = restTemplate.getForObject(url, Product.class);

        if (product == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        return product.getStock();
    }

    // Actualizar stock tras una compra
    public void updateStock(Long productId, int quantityToReduce) {
        String url = productServiceUrl + "/" + productId;
        Product product = restTemplate.getForObject(url, Product.class);

        if (product == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        int newStock = Math.max(product.getStock() - quantityToReduce, 0);
        product.setStock(newStock);

        restTemplate.put(url, product);

        // Evento simulado: log de actualización
        System.out.println("📦 Evento: Inventario actualizado → Producto " + productId + " nuevo stock: " + newStock);
    }
}

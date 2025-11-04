package com.example.producto.service;

import com.example.producto.model.Product;

import java.util.List;

public interface ProductService {
    Product create(Product product);
    Product update(Long id, Product product);
    Product getById(Long id);
    List<Product> getAll();
    void delete(Long id);
}

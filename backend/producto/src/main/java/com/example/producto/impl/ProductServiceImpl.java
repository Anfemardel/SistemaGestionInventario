package com.example.producto.impl;

import com.example.producto.exception.ResourceNotFoundException;
import com.example.producto.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.producto.repository.ProductRepository;
import com.example.producto.service.ProductService;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public Product create(Product product) {
        if (product.getSku() == null || product.getSku().isEmpty()) {
            product.setSku(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        return repo.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        if (product.getSku() == null || product.getSku().isEmpty()) {
            product.setSku(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        existing.setSku(product.getSku());
        return repo.save(existing);
    }

    @Override
    public Product getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    @Override
    public List<Product> getAll() {
        return repo.findAll();
    }

    @Override
    public void delete(Long id) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        repo.delete(existing);
    }
}

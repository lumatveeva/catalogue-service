package ru.lumat.catalogueservice.service;

import ru.lumat.catalogueservice.entity.Product;

import java.util.Optional;

public interface ProductService {
    Iterable<Product> findAllProducts();

    Product createProduct(String title, String details);

    Optional<Product> findProduct(int productId);

    void updateProduct(Integer id, String title, String details);

    void deleteProduct(Integer id);
}

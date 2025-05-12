package ru.lumat.catalogueservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lumat.catalogueservice.entity.Product;
import ru.lumat.catalogueservice.repository.ProductRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DefaultProductService implements ProductService {
    public final ProductRepository productRepository;

    public DefaultProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Iterable<Product> findAllProducts(String filter) {
        if (filter != null && !filter.isEmpty()) {
            return productRepository.findProductListByFilterNative("%" + filter + "%");
        }
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(String title, String details) {
        return productRepository.save(new Product(null, title, details));
    }

    @Override
    public Optional<Product> findProduct(int productId) {
        return productRepository.findById(productId);
    }

    @Override
    @Transactional
    public void updateProduct(Integer id, String title, String details) {
        productRepository.findById(id)
                .ifPresentOrElse(product -> {
                    product.setTitle(title);
                    product.setDetails(details);
                }, () -> {
                    throw new NoSuchElementException();
                });
    }

    @Override
    public void deleteProduct(Integer id) {
        this.productRepository.deleteById(id);
    }
}

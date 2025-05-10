package ru.lumat.catalogueservice.repository;

import org.springframework.data.repository.CrudRepository;
import ru.lumat.catalogueservice.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}

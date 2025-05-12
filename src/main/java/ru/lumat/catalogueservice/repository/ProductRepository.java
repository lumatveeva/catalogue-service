package ru.lumat.catalogueservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.lumat.catalogueservice.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    Iterable<Product> findAllByTitleLikeIgnoreCase(String filter);

    // формирование запроса с помощью jpql, работа с сущностью, а не с колонками
    @Query(value = "select p from Product p where p.title ilike :filter")
    Iterable<Product> findProductListByFilter(@Param("filter") String filter);

    // формирование запроса с помощью sql, работа с таблицей и колонками
    @Query(value = "select * from catalogue.t_product where c_title ilike :filter",
            nativeQuery = true)
    Iterable<Product> findProductListByFilterNative(@Param("filter") String filter);

    //сам запрос находится в клаасе сущности, по названию запроса он подтягивается
    @Query(name = "Product.findAllProductsByTitleFilterIgnoreCase")
    Iterable<Product> findProductListByTitleFilterIgnoreCase(@Param("filter") String filter);
}

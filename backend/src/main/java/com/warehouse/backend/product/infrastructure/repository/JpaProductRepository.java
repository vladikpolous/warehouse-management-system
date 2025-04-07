package com.warehouse.backend.product.infrastructure.repository;

import com.warehouse.backend.product.domain.model.Product;
import com.warehouse.backend.product.domain.repository.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaProductRepository extends ProductRepository, JpaRepository<Product, Long> {

    @Override
    default List<Product> getAllProducts() {
        return findAll();
    }

    @Override
    default Optional<Product> getProductById(Long id) {
        return findById(id);
    }

    @Override
    default Product createNewProduct(Product product) {
        return save(product);
    }

    @Override
    default void deleteProductById(Long id) {
        findById(id).ifPresent(this::delete);
    }

    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE LOWER(p.name) = LOWER(:name)")
    boolean isProductExist(@Param("name") String name);
}

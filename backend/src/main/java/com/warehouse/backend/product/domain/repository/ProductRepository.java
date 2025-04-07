package com.warehouse.backend.product.domain.repository;

import com.warehouse.backend.product.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> getAllProducts();

    Optional<Product> getProductById(Long id);

    Product createNewProduct(Product product);

    void deleteProductById(Long id);

    boolean isProductExist(String name);
}

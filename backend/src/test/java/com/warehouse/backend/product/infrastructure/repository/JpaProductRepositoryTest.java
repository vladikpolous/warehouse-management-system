package com.warehouse.backend.product.infrastructure.repository;

import com.warehouse.backend.category.domain.model.Category;
import com.warehouse.backend.category.infrastructure.repository.JpaCategoryRepository;
import com.warehouse.backend.product.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaProductRepositoryTest {

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Autowired
    private JpaCategoryRepository jpaCategoryRepository;

    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        jpaProductRepository.deleteAll();
        jpaCategoryRepository.deleteAll();

        testCategory = new Category();
        testCategory.setName("Test category");
        testCategory.setDescription("Test category description");
        testCategory = jpaCategoryRepository.save(testCategory);

        testProduct = new Product();
        testProduct.setName("Test product");
        testProduct.setDescription("Test product description");
        testProduct.setCategory(testCategory);
        testProduct.setCreatedDate(LocalDateTime.now());
    }

    @Test
    void testCreateNewProduct() {
        Product savedProduct = jpaProductRepository.createNewProduct(testProduct);

        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(testProduct.getName());
        assertThat(savedProduct.getDescription()).isEqualTo(testProduct.getDescription());
        assertThat(savedProduct.getCategory().getId()).isEqualTo(testCategory.getId());
        assertThat(savedProduct.getCreatedDate()).isNotNull();
    }

    @Test
    void testGetAllProducts() {
        jpaProductRepository.createNewProduct(testProduct);

        Product secondProduct = new Product();
        secondProduct.setName("Second product");
        secondProduct.setDescription("Second product description");
        secondProduct.setCategory(testCategory);
        secondProduct.setCreatedDate(LocalDateTime.now());
        jpaProductRepository.createNewProduct(secondProduct);

        List<Product> products = jpaProductRepository.getAllProducts();

        assertThat(products).hasSize(2);
        assertThat(products).extracting(Product::getName)
                .containsExactlyInAnyOrder("Test product", "Second product");
    }

    @Test
    void testGetProductById() {
        Product savedProduct = jpaProductRepository.createNewProduct(testProduct);

        Optional<Product> foundProduct = jpaProductRepository.getProductById(savedProduct.getId());

        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getId()).isEqualTo(savedProduct.getId());
        assertThat(foundProduct.get().getName()).isEqualTo(savedProduct.getName());
    }

    @Test
    void testDeleteProductById() {
        Product savedProduct = jpaProductRepository.createNewProduct(testProduct);

        jpaProductRepository.deleteProductById(savedProduct.getId());

        Optional<Product> deletedProduct = jpaProductRepository.getProductById(savedProduct.getId());
        assertThat(deletedProduct).isEmpty();
    }

    @Test
    void testIsProductExist() {
        jpaProductRepository.createNewProduct(testProduct);

        boolean exists = jpaProductRepository.isProductExist("Test product");
        boolean notExists = jpaProductRepository.isProductExist("Non-existent product");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}

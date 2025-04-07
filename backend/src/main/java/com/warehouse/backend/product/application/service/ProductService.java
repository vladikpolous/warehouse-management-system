package com.warehouse.backend.product.application.service;

import com.warehouse.backend.category.domain.exception.CategoryNotFoundException;
import com.warehouse.backend.category.domain.model.Category;
import com.warehouse.backend.category.domain.repository.CategoryRepository;
import com.warehouse.backend.product.application.mapper.ProductMapper;
import com.warehouse.backend.product.application.port.input.CreateProductRequest;
import com.warehouse.backend.product.application.port.output.ProductDto;
import com.warehouse.backend.product.domain.exception.ProductAlreadyExistsException;
import com.warehouse.backend.product.domain.exception.ProductNotFoundException;
import com.warehouse.backend.product.domain.model.Product;
import com.warehouse.backend.product.domain.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Cacheable(value = "allProducts", key = "'list'")
    public List<ProductDto> getAllProducts() {
        logger.info("Getting all products");
        List<ProductDto> products = productRepository.getAllProducts().stream()
                .map(productMapper::productToProductDto)
                .toList();
        logger.info("Successfully retrieved {} products", products.size());
        return products;
    }

    @Cacheable(value = "productById", key = "#root.args[0]")
    public ProductDto getProductById(Long id) {
        logger.info("Getting product by id: {}", id);
        Product product = productRepository.getProductById(id).orElseThrow(() -> new ProductNotFoundException(id));
        logger.info("Found product with name: {}", product.getName());
        return productMapper.productToProductDto(product);
    }

    @CacheEvict(value = "allProducts", key = "'list'")
    @Transactional
    public ProductDto saveProduct(CreateProductRequest productRequest) {
        logger.info("Creating new product with name: {}", productRequest.getName());
        if (productRepository.isProductExist(productRequest.getName())) {
            logger.warn("Product with name '{}' already exists", productRequest.getName());
            throw new ProductAlreadyExistsException(productRequest.getName());
        }

        Category category = categoryRepository.getCategoryById(productRequest.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(productRequest.getCategoryId()));

        Product product = productMapper.createProductRequestToProduct(productRequest);
        product.setCategory(category);
        product.setCreatedDate(LocalDateTime.now());

        Product newProduct = productRepository.createNewProduct(product);
        logger.info("Product created successfully with id: {}", newProduct.getId());
        return productMapper.productToProductDto(newProduct);
    }

    @Caching(put = @CachePut(value = "productById", key = "#root.args[1]"),
            evict = @CacheEvict(value = "allProducts", key = "'list'"))
    @Transactional
    public ProductDto updateProduct(CreateProductRequest productRequest, Long id) {
        logger.info("Updating product with id: {}", id);
        Product existingProduct = productRepository.getProductById(id).orElseThrow(() -> new ProductNotFoundException(id));
        logger.info("Found product to update. Old name: {}. New name: {}", existingProduct.getName(), productRequest.getName());
        
        if (!existingProduct.getName().equals(productRequest.getName()) &&
                productRepository.isProductExist(productRequest.getName())) {
            logger.warn("Product with name '{}' already exists", productRequest.getName());
            throw new ProductAlreadyExistsException(productRequest.getName());
        }

        Category category = categoryRepository.getCategoryById(productRequest.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(productRequest.getCategoryId()));

        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setCategory(category);

        Product updatedProduct = productRepository.createNewProduct(existingProduct);
        logger.info("Product updated successfully with id: {}", updatedProduct.getId());
        return productMapper.productToProductDto(updatedProduct);
    }

    @Caching(evict = {
            @CacheEvict(value = "productById", key = "#root.args[0]"),
            @CacheEvict(value = "allProducts", key = "'list'")
    })
    @Transactional
    public void deleteProduct(Long id) {
        logger.info("Deleting product with id: {}", id);
        if (productRepository.getProductById(id).isEmpty()) {
            logger.warn("Product with id {} not found for deletion", id);
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteProductById(id);
        logger.info("Product with id {} deleted successfully", id);
    }
}

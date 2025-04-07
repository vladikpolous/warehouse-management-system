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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDto productDto;
    private Category category;
    private CreateProductRequest createProductRequest;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setCategory(category);
        product.setCreatedDate(LocalDateTime.now());

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");

        createProductRequest = new CreateProductRequest();
        createProductRequest.setName("Test Product");
        createProductRequest.setDescription("Test Description");
        createProductRequest.setCategoryId(1L);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        when(productRepository.getAllProducts()).thenReturn(List.of(product));
        when(productMapper.productToProductDto(any(Product.class))).thenReturn(productDto);

        List<ProductDto> result = productService.getAllProducts();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Test Product");
        verify(productRepository).getAllProducts();
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        when(productRepository.getProductById(anyLong())).thenReturn(Optional.of(product));
        when(productMapper.productToProductDto(any(Product.class))).thenReturn(productDto);

        ProductDto result = productService.getProductById(1L);

        assertThat(result.getName()).isEqualTo("Test Product");
        verify(productRepository).getProductById(1L);
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldThrowException() {
        when(productRepository.getProductById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
        verify(productRepository).getProductById(1L);
    }

    @Test
    void saveProduct_WhenProductNameDoesNotExist_ShouldSaveProduct() {
        when(productRepository.isProductExist(anyString())).thenReturn(false);
        when(categoryRepository.getCategoryById(anyLong())).thenReturn(Optional.of(category));
        when(productMapper.createProductRequestToProduct(any(CreateProductRequest.class))).thenReturn(product);
        when(productRepository.createNewProduct(any(Product.class))).thenReturn(product);
        when(productMapper.productToProductDto(any(Product.class))).thenReturn(productDto);

        ProductDto result = productService.saveProduct(createProductRequest);

        assertThat(result.getName()).isEqualTo("Test Product");
        verify(productRepository).isProductExist("Test Product");
        verify(categoryRepository).getCategoryById(1L);
        verify(productRepository).createNewProduct(any(Product.class));
    }

    @Test
    void saveProduct_WhenProductNameExists_ShouldThrowException() {
        when(productRepository.isProductExist(anyString())).thenReturn(true);

        assertThrows(ProductAlreadyExistsException.class, () -> productService.saveProduct(createProductRequest));
        verify(productRepository).isProductExist("Test Product");
        verify(productRepository, never()).createNewProduct(any(Product.class));
    }

    @Test
    void saveProduct_WhenCategoryDoesNotExist_ShouldThrowException() {
        when(productRepository.isProductExist(anyString())).thenReturn(false);
        when(categoryRepository.getCategoryById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> productService.saveProduct(createProductRequest));
        verify(productRepository).isProductExist("Test Product");
        verify(categoryRepository).getCategoryById(1L);
        verify(productRepository, never()).createNewProduct(any(Product.class));
    }

    @Test
    void updateProduct_WhenProductExistsAndNameNotTaken_ShouldUpdateProduct() {
        when(productRepository.getProductById(anyLong())).thenReturn(Optional.of(product));
        when(categoryRepository.getCategoryById(anyLong())).thenReturn(Optional.of(category));
        when(productRepository.createNewProduct(any(Product.class))).thenReturn(product);
        when(productMapper.productToProductDto(any(Product.class))).thenReturn(productDto);

        ProductDto result = productService.updateProduct(createProductRequest, 1L);

        assertThat(result.getName()).isEqualTo("Test Product");
        verify(productRepository).getProductById(1L);
        verify(categoryRepository).getCategoryById(1L);
        verify(productRepository).createNewProduct(any(Product.class));
    }

    @Test
    void updateProduct_WhenProductDoesNotExist_ShouldThrowException() {
        when(productRepository.getProductById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(createProductRequest, 1L));
        verify(productRepository).getProductById(1L);
        verify(productRepository, never()).createNewProduct(any(Product.class));
    }

    @Test
    void updateProduct_WhenNewNameIsTaken_ShouldThrowException() {
        CreateProductRequest newRequest = new CreateProductRequest();
        newRequest.setName("New Name");
        newRequest.setDescription("New Description");
        newRequest.setCategoryId(1L);

        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Old Name");
        existingProduct.setDescription("Old Description");
        existingProduct.setCategory(category);

        when(productRepository.getProductById(anyLong())).thenReturn(Optional.of(existingProduct));
        when(productRepository.isProductExist("New Name")).thenReturn(true);

        assertThrows(ProductAlreadyExistsException.class, () -> productService.updateProduct(newRequest, 1L));
        verify(productRepository).getProductById(1L);
        verify(productRepository).isProductExist("New Name");
        verify(productRepository, never()).createNewProduct(any(Product.class));
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldDeleteProduct() {
        when(productRepository.getProductById(anyLong())).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository).getProductById(1L);
        verify(productRepository).deleteProductById(1L);
    }

    @Test
    void deleteProduct_WhenProductDoesNotExist_ShouldThrowException() {
        when(productRepository.getProductById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
        verify(productRepository).getProductById(1L);
        verify(productRepository, never()).deleteProductById(anyLong());
    }
}

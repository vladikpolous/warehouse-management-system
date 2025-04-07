package com.warehouse.backend.product.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.backend.category.application.port.output.CategoryDto;
import com.warehouse.backend.product.application.port.input.CreateProductRequest;
import com.warehouse.backend.product.application.port.output.ProductDto;
import com.warehouse.backend.product.application.service.ProductService;
import com.warehouse.backend.product.domain.exception.ProductAlreadyExistsException;
import com.warehouse.backend.product.domain.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    private ProductDto productDto;
    private CreateProductRequest createProductRequest;

    @BeforeEach
    void setUp() {
        CategoryDto categoryDto = new CategoryDto(1L, "Test Category", "Test Category Description");

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");
        productDto.setCategory(categoryDto);
        productDto.setCreatedDate(LocalDateTime.now());

        createProductRequest = new CreateProductRequest();
        createProductRequest.setName("Test Product");
        createProductRequest.setDescription("Test Description");
        createProductRequest.setCategoryId(1L);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(productDto));

        mockMvc.perform(get("/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Product"))
                .andExpect(jsonPath("$[0].description").value("Test Description"))
                .andExpect(jsonPath("$[0].category.id").value(1))
                .andExpect(jsonPath("$[0].category.name").value("Test Category"));
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() throws Exception {
        when(productService.getProductById(anyLong())).thenReturn(productDto);

        mockMvc.perform(get("/product/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.category.id").value(1))
                .andExpect(jsonPath("$.category.name").value("Test Category"));
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(productService.getProductById(anyLong())).thenThrow(new ProductNotFoundException(1L));

        mockMvc.perform(get("/product/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct_WhenValidInput_ShouldCreateProduct() throws Exception {
        when(productService.saveProduct(any(CreateProductRequest.class))).thenReturn(productDto);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProductRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.category.id").value(1))
                .andExpect(jsonPath("$.category.name").value("Test Category"));
    }

    @Test
    void createProduct_WhenProductNameExists_ShouldReturnConflict() throws Exception {
        when(productService.saveProduct(any(CreateProductRequest.class)))
                .thenThrow(new ProductAlreadyExistsException("Test Product"));

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProductRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateProduct_WhenValidInput_ShouldUpdateProduct() throws Exception {
        when(productService.updateProduct(any(CreateProductRequest.class), anyLong())).thenReturn(productDto);

        mockMvc.perform(put("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProductRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.category.id").value(1))
                .andExpect(jsonPath("$.category.name").value("Test Category"));
    }

    @Test
    void updateProduct_WhenProductDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(productService.updateProduct(any(CreateProductRequest.class), anyLong()))
                .thenThrow(new ProductNotFoundException(1L));

        mockMvc.perform(put("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProductRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/product/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_WhenProductDoesNotExist_ShouldReturnNotFound() throws Exception {
        doThrow(new ProductNotFoundException(1L)).when(productService).deleteProduct(anyLong());

        mockMvc.perform(delete("/product/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

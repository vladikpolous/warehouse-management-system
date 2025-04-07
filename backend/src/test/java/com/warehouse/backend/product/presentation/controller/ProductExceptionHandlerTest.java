package com.warehouse.backend.product.presentation.controller;

import com.warehouse.backend.common.dto.ErrorResponse;
import com.warehouse.backend.product.domain.exception.ProductAlreadyExistsException;
import com.warehouse.backend.product.domain.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductExceptionHandlerTest {

    @InjectMocks
    private ProductExceptionHandler productExceptionHandler;

    @Test
    void handleProductNotFoundException_ShouldReturnNotFoundStatus() {
        Long productId = 1L;
        ProductNotFoundException exception = new ProductNotFoundException(productId);

        ResponseEntity<ErrorResponse> response = productExceptionHandler.handleProductNotFoundException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Product with ID " + productId + " not found.");
    }

    @Test
    void handleProductAlreadyExistsException_ShouldReturnConflictStatus() {
        String productName = "Test Product";
        ProductAlreadyExistsException exception = new ProductAlreadyExistsException(productName);

        ResponseEntity<ErrorResponse> response = productExceptionHandler.handleProductAlreadyExistsException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Product with name '" + productName + "' already exists");
    }
}

package com.warehouse.backend.category.presentation.controller;

import com.warehouse.backend.category.domain.exception.CategoryAlreadyExistsException;
import com.warehouse.backend.category.domain.exception.CategoryNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CategoryExceptionHandlerTest {

    @InjectMocks
    private CategoryExceptionHandler categoryExceptionHandler;

    @Test
    void handleCategoryNotFoundException_ShouldReturnNotFoundStatus() {
        Long categoryId = 1L;
        CategoryNotFoundException exception = new CategoryNotFoundException(categoryId);

        ResponseEntity<String> response = categoryExceptionHandler.handleCategoryNotFoundException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Category with ID " + categoryId + " not found.");
    }

    @Test
    void handleCategoryAlreadyExistsException_ShouldReturnConflictStatus() {
        // Arrange
        String categoryName = "Test Category";
        CategoryAlreadyExistsException exception = new CategoryAlreadyExistsException(categoryName);

        ResponseEntity<String> response = categoryExceptionHandler.handleCategoryAlreadyExistsException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isEqualTo("Category with name '" + categoryName + "' already exists");
    }
}

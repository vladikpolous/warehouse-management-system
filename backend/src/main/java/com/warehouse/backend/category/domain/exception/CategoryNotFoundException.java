package com.warehouse.backend.category.domain.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(Long id) {
        super("Category with ID " + id + " not found.");
    }
}

package com.warehouse.backend.category.presentation.controller;

import com.warehouse.backend.category.application.port.input.CreateCategoryRequest;
import com.warehouse.backend.category.application.port.output.CategoryDto;
import com.warehouse.backend.category.application.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Tag(name = "Category API", description = "API for category management")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Get all categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get certain category by ID")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new category")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CreateCategoryRequest categoryDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.saveCategory(categoryDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Change category information")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("id") Long id, @RequestBody CreateCategoryRequest createCategoryRequest) {
        return ResponseEntity.ok(categoryService.updateCategory(createCategoryRequest, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category by ID")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }
}

package com.warehouse.backend.category.domain.repository;

import com.warehouse.backend.category.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    List<Category> getAllCategories();

    Optional<Category> getCategoryById(Long id);

    Category createNewCategory(Category category);

    void deleteCategoryById(Long id);
}

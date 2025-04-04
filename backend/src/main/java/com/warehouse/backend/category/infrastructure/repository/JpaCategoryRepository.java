package com.warehouse.backend.category.infrastructure.repository;

import com.warehouse.backend.category.domain.model.Category;
import com.warehouse.backend.category.domain.repository.CategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaCategoryRepository extends CategoryRepository, JpaRepository<Category, Long> {

    @Override
    default List<Category> getAllCategories() {
        return findAll();
    }

    @Override
    default Optional<Category> getCategoryById(Long id) {
        return findById(id);
    }

    @Override
    default Category createNewCategory(Category category) {
        return save(category);
    }

    @Override
    default void deleteCategoryById(Long id) {
        findById(id).ifPresent(this::delete);
    }

    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE LOWER(c.name) = LOWER(:name)")
    boolean isCategoryExist(@Param("name") String name);
}
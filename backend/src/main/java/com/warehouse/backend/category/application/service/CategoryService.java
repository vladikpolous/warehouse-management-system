package com.warehouse.backend.category.application.service;

import com.warehouse.backend.category.application.mapper.CategoryMapper;
import com.warehouse.backend.category.application.port.input.CreateCategoryRequest;
import com.warehouse.backend.category.application.port.output.CategoryDto;
import com.warehouse.backend.category.domain.exception.CategoryNotFoundException;
import com.warehouse.backend.category.domain.model.Category;
import com.warehouse.backend.category.domain.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.getAllCategories().stream()
                .map(categoryMapper::categoryToCategoryDto)
                .toList();
    }

    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.getCategoryById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        return categoryMapper.categoryToCategoryDto(category);
    }

    public CategoryDto saveCategory(CreateCategoryRequest category) {
        Category newCategory = categoryRepository.createNewCategory(categoryMapper.createCategoryRequestToCategory(category));
        return categoryMapper.categoryToCategoryDto(newCategory);
    }

    public CategoryDto updateCategory(CreateCategoryRequest createCategoryRequest, Long id) {
        Category existingCategory = categoryRepository.getCategoryById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        existingCategory.setName(createCategoryRequest.getName());
        existingCategory.setDescription(createCategoryRequest.getDescription());
        return categoryMapper.categoryToCategoryDto(categoryRepository.createNewCategory(existingCategory));
    }

    public void deleteCategoryById(Long id) {
        if (categoryRepository.getCategoryById(id).isPresent()) {
            categoryRepository.deleteCategoryById(id);
        } else {
            throw new CategoryNotFoundException(id);
        }
    }
}

package com.warehouse.backend.category.application.service;

import com.warehouse.backend.category.application.mapper.CategoryMapper;
import com.warehouse.backend.category.application.port.input.CreateCategoryRequest;
import com.warehouse.backend.category.application.port.output.CategoryDto;
import com.warehouse.backend.category.domain.exception.CategoryAlreadyExistsException;
import com.warehouse.backend.category.domain.exception.CategoryNotFoundException;
import com.warehouse.backend.category.domain.model.Category;
import com.warehouse.backend.category.domain.repository.CategoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private static final Logger logger = LogManager.getLogger(CategoryService.class);

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Cacheable(value = "allCategories", key = "'list'")
    public List<CategoryDto> getAllCategories() {
        logger.info("Getting all categories");
        return categoryRepository.getAllCategories().stream()
                .map(categoryMapper::categoryToCategoryDto)
                .toList();
    }

    @Cacheable(value = "categoryById", key = "#root.args[0]")
    public CategoryDto getCategoryById(Long id) {
        logger.info("Getting category by id: {}", id);
        Category category = categoryRepository.getCategoryById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        logger.info("Found category with name: {}", category.getName());
        return categoryMapper.categoryToCategoryDto(category);
    }

    @CacheEvict(value = "allCategories", key = "'list'")
    public CategoryDto saveCategory(CreateCategoryRequest category) {
        logger.info("Creating new category with name: {}", category.getName());
        if (categoryRepository.isCategoryExist(category.getName())) {
            logger.warn("Category with name '{}' already exists", category.getName());
            throw new CategoryAlreadyExistsException(category.getName());
        }
        Category newCategory = categoryRepository.createNewCategory(categoryMapper.createCategoryRequestToCategory(category));
        logger.info("Category created successfully with id: {}", newCategory.getId());
        return categoryMapper.categoryToCategoryDto(newCategory);
    }

    @Caching(put = @CachePut(value = "categoryById", key = "#root.args[1]"),
            evict = @CacheEvict(value = "allCategories", key = "'list'"))
    public CategoryDto updateCategory(CreateCategoryRequest createCategoryRequest, Long id) {
        logger.info("Updating category with id: {}", id);
        Category existingCategory = categoryRepository.getCategoryById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        logger.info("Found category to update. Old name: {}. New name: {}", existingCategory.getName(), createCategoryRequest.getName());
        if (!existingCategory.getName().equals(createCategoryRequest.getName()) &&
                categoryRepository.isCategoryExist(createCategoryRequest.getName())) {
            logger.warn("Cannot update category. Category with name '{}' already exists", createCategoryRequest.getName());
            throw new CategoryAlreadyExistsException(createCategoryRequest.getName());
        }
        existingCategory.setName(createCategoryRequest.getName());
        existingCategory.setDescription(createCategoryRequest.getDescription());
        Category updatedCategory = categoryRepository.createNewCategory(existingCategory);
        logger.info("Category updated successfully");
        return categoryMapper.categoryToCategoryDto(updatedCategory);
    }

    @Caching(evict = {
            @CacheEvict(value = "categoryById", key = "#root.args[0]"),
            @CacheEvict(value = "allCategories", key = "'list'")})
    public void deleteCategoryById(Long id) {
        logger.info("Attempting to delete category with id: {}", id);
        if (categoryRepository.getCategoryById(id).isPresent()) {
            categoryRepository.deleteCategoryById(id);
            logger.info("Category with id: {} deleted successfully", id);
        } else {
            logger.warn("Cannot delete category with id: {} - not found", id);
            throw new CategoryNotFoundException(id);
        }
    }
}

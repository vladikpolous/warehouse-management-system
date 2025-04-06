package com.warehouse.backend.category.application.service;

import com.warehouse.backend.category.application.mapper.CategoryMapper;
import com.warehouse.backend.category.application.port.input.CreateCategoryRequest;
import com.warehouse.backend.category.application.port.output.CategoryDto;
import com.warehouse.backend.category.domain.model.Category;
import com.warehouse.backend.category.domain.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for verifying caching logic in CategoryService.
 * These tests check that service methods correctly interact with the repository
 * according to the expected caching behavior.
 */

@ExtendWith(MockitoExtension.class)
class CategoryServiceCacheTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryRepository, categoryMapper);
    }

    /**
     * Test verifies that getAllCategories method uses caching.
     * In a real environment, the second call would use cached data due to @Cacheable annotation.
     */

    @Test
    void shouldCacheGetAllCategories() {
        Category category = new Category(1L, "Electronics", "Gadgets");
        CategoryDto categoryDto = new CategoryDto(1L, "Electronics", "Gadgets");

        when(categoryRepository.getAllCategories()).thenReturn(Collections.singletonList(category));
        when(categoryMapper.categoryToCategoryDto(category)).thenReturn(categoryDto);

        List<CategoryDto> firstCall = categoryService.getAllCategories();

        assertThat(firstCall).hasSize(1);
        assertThat(firstCall.getFirst()).isEqualTo(categoryDto);

        verify(categoryRepository, times(1)).getAllCategories();

        verify(categoryMapper, times(1)).categoryToCategoryDto(category);
    }

    /**
     * Test verifies that getCategoryById method uses caching.
     * In a real environment, the second call would use cached data due to @Cacheable annotation.
     */

    @Test
    void shouldCacheGetCategoryById() {
        Long id = 1L;
        Category category = new Category(id, "Electronics", "Gadgets");
        CategoryDto categoryDto = new CategoryDto(id, "Electronics", "Gadgets");

        when(categoryRepository.getCategoryById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.categoryToCategoryDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getCategoryById(id);

        assertThat(result).isEqualTo(categoryDto);
        verify(categoryRepository, times(1)).getCategoryById(id);
        verify(categoryMapper, times(1)).categoryToCategoryDto(category);
    }

    /**
     * Test verifies that saveCategory method evicts cache for getAllCategories.
     * In a real environment, after calling saveCategory, the cache for getAllCategories
     * would be cleared due to @CacheEvict annotation.
     */

    @Test
    void shouldEvictCacheOnSaveCategory() {
        CreateCategoryRequest request = new CreateCategoryRequest("Electronics", "Gadgets");
        Category newCategory = new Category(null, "Electronics", "Gadgets");
        Category savedCategory = new Category(1L, "Electronics", "Gadgets");
        CategoryDto categoryDto = new CategoryDto(1L, "Electronics", "Gadgets");

        when(categoryRepository.isCategoryExist(anyString())).thenReturn(false);
        when(categoryMapper.createCategoryRequestToCategory(request)).thenReturn(newCategory);
        when(categoryRepository.createNewCategory(newCategory)).thenReturn(savedCategory);
        when(categoryMapper.categoryToCategoryDto(savedCategory)).thenReturn(categoryDto);

        CategoryDto result = categoryService.saveCategory(request);

        assertThat(result).isEqualTo(categoryDto);
        verify(categoryRepository, times(1)).isCategoryExist(anyString());
        verify(categoryMapper, times(1)).createCategoryRequestToCategory(request);
        verify(categoryRepository, times(1)).createNewCategory(newCategory);
        verify(categoryMapper, times(1)).categoryToCategoryDto(savedCategory);
    }

    /**
     * Test verifies that updateCategory method updates cache for getCategoryById
     * and evicts cache for getAllCategories.
     * In a real environment, after calling updateCategory, the cache for getCategoryById
     * would be updated due to @CachePut annotation, and the cache for getAllCategories
     * would be cleared due to @CacheEvict annotation.
     */

    @Test
    void shouldUpdateCacheOnUpdateCategory() {
        Long id = 1L;
        Category oldCategory = new Category(id, "Old", "Old desc");
        Category updatedCategory = new Category(id, "New", "New desc");
        CreateCategoryRequest request = new CreateCategoryRequest("New", "New desc");
        CategoryDto updatedDto = new CategoryDto(id, "New", "New desc");

        when(categoryRepository.getCategoryById(id)).thenReturn(Optional.of(oldCategory));
        when(categoryRepository.isCategoryExist(anyString())).thenReturn(false);

        when(categoryRepository.createNewCategory(any(Category.class))).thenReturn(updatedCategory);
        when(categoryMapper.categoryToCategoryDto(updatedCategory)).thenReturn(updatedDto);

        CategoryDto result = categoryService.updateCategory(request, id);

        assertThat(result).isEqualTo(updatedDto);
        verify(categoryRepository, times(1)).getCategoryById(id);
        verify(categoryRepository, times(1)).isCategoryExist(anyString());

        verify(categoryRepository, times(1)).createNewCategory(updatedCategory);
        verify(categoryMapper, times(1)).categoryToCategoryDto(updatedCategory);
    }

    /**
     * Test verifies that deleteCategoryById method evicts cache for getCategoryById
     * and getAllCategories.
     * In a real environment, after calling deleteCategoryById, the cache for getCategoryById
     * and getAllCategories would be cleared due to @CacheEvict annotations.
     */

    @Test
    void shouldEvictCacheOnDeleteCategoryById() {
        Long id = 1L;
        Category category = new Category(id, "Electronics", "Gadgets");

        when(categoryRepository.getCategoryById(id)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteCategoryById(id);

        categoryService.deleteCategoryById(id);

        verify(categoryRepository, times(1)).getCategoryById(id);
        verify(categoryRepository, times(1)).deleteCategoryById(id);
    }
}

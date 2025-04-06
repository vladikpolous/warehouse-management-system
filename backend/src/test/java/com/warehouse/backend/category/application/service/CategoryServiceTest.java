package com.warehouse.backend.category.application.service;

import com.warehouse.backend.category.application.mapper.CategoryMapper;
import com.warehouse.backend.category.application.port.input.CreateCategoryRequest;
import com.warehouse.backend.category.application.port.output.CategoryDto;
import com.warehouse.backend.category.domain.exception.CategoryAlreadyExistsException;
import com.warehouse.backend.category.domain.exception.CategoryNotFoundException;
import com.warehouse.backend.category.domain.model.Category;
import com.warehouse.backend.category.domain.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void testGetAllCategories_ShouldReturnEmptyList_WhenNoCategoriesExist() {
        when(categoryRepository.getAllCategories()).thenReturn(emptyList());

        List<CategoryDto> categories = categoryService.getAllCategories();
        assertTrue(categories.isEmpty());
        verify(categoryRepository, times(1)).getAllCategories();
    }

    @Test
    void testGetAllCategories_ShouldReturnCategories_WhenCategoriesExist() {
        Category category = new Category(1L, "Test category", "Test description");
        CategoryDto categoryDto = new CategoryDto(1L, "Test category", "Test description");

        when(categoryRepository.getAllCategories()).thenReturn(List.of(category));
        when(categoryMapper.categoryToCategoryDto(category)).thenReturn(categoryDto);

        List<CategoryDto> result = categoryService.getAllCategories();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(categoryDto);
        verify(categoryRepository, times(1)).getAllCategories();
        verify(categoryMapper, times(1)).categoryToCategoryDto(category);
    }

    @Test
    void testGetCategoryById_ShouldReturnCategory_WhenCategoryExists() {
        Category category = new Category(1L, "Test category", "Test description");
        CategoryDto categoryDto = new CategoryDto(1L, "Test category", "Test description");

        when(categoryRepository.getCategoryById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.categoryToCategoryDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getCategoryById(1L);

        assertThat(result).isEqualTo(categoryDto);
        verify(categoryRepository, times(1)).getCategoryById(1L);
        verify(categoryMapper, times(1)).categoryToCategoryDto(category);
    }

    @Test
    void testGetCategoryById_ShouldThrowException_WhenCategoryDoesNotExist() {
        when(categoryRepository.getCategoryById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(1L));
        verify(categoryRepository, times(1)).getCategoryById(1L);
    }

    @Test
    void testSaveCategory_ShouldSaveCategory_WhenCategoryDoesNotExist() {
        CreateCategoryRequest request = new CreateCategoryRequest("Test category", "Test description");
        Category category = new Category(1L, "Test category", "Test description");
        Category savedCategory = new Category(1L, "Test category", "Test description");
        CategoryDto categoryDto = new CategoryDto(1L, "Test category", "Test description");

        when(categoryMapper.createCategoryRequestToCategory(request)).thenReturn(category);
        when(categoryRepository.isCategoryExist("Test category")).thenReturn(false);
        when(categoryRepository.createNewCategory(category)).thenReturn(savedCategory);
        when(categoryMapper.categoryToCategoryDto(savedCategory)).thenReturn(categoryDto);

        CategoryDto result = categoryService.saveCategory(request);

        assertThat(result).isEqualTo(categoryDto);
        verify(categoryMapper, times(1)).createCategoryRequestToCategory(request);
        verify(categoryRepository, times(1)).createNewCategory(category);
        verify(categoryMapper, times(1)).categoryToCategoryDto(savedCategory);
    }

    @Test
    void testSaveCategory_ShouldThrowException_WhenCategoryAlreadyExists() {
        CreateCategoryRequest request = new CreateCategoryRequest("Test category", "Test description");

        when(categoryRepository.isCategoryExist("Test category")).thenReturn(true);

        assertThrows(CategoryAlreadyExistsException.class, () -> categoryService.saveCategory(request));
        verify(categoryRepository, times(1)).isCategoryExist("Test category");
    }

    @Test
    void testUpdateCategory_ShouldUpdateCategory_WhenCategoryExists() {
        CreateCategoryRequest request = new CreateCategoryRequest("Updated category", "Updated description");
        Category existingCategory = new Category(1L, "Test category", "Test description");
        Category updatedCategory = new Category(1L, "Updated category", "Updated description");
        CategoryDto categoryDto = new CategoryDto(1L, "Updated category", "Updated description");

        when(categoryRepository.getCategoryById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.isCategoryExist("Updated category")).thenReturn(false);
        when(categoryRepository.createNewCategory(updatedCategory)).thenReturn(updatedCategory);
        when(categoryMapper.categoryToCategoryDto(updatedCategory)).thenReturn(categoryDto);

        CategoryDto result = categoryService.updateCategory(request, 1L);

        assertThat(result).isEqualTo(categoryDto);
        verify(categoryRepository, times(1)).getCategoryById(1L);
        verify(categoryRepository, times(1)).isCategoryExist("Updated category");
        verify(categoryRepository, times(1)).createNewCategory(updatedCategory);
        verify(categoryMapper, times(1)).categoryToCategoryDto(updatedCategory);
    }

    @Test
    void testUpdateCategory_ShouldThrowException_WhenCategoryDoesNotExist() {
        CreateCategoryRequest request = new CreateCategoryRequest("Updated category", "Updated description");

        when(categoryRepository.getCategoryById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(request, 1L));
        verify(categoryRepository, times(1)).getCategoryById(1L);
    }

    @Test
    void testDeleteCategory_ShouldDeleteCategory_WhenCategoryExist() {
        Category category = new Category(1L, "Test category", "Test description");

        when(categoryRepository.getCategoryById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategoryById(1L);

        verify(categoryRepository, times(1)).getCategoryById(1L);
        verify(categoryRepository, times(1)).deleteCategoryById(1L);
    }

    @Test
    void testDeleteCategory_ShouldThrowException_WhenCategoryDoesNotExist() {
        when(categoryRepository.getCategoryById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategoryById(1L));
        verify(categoryRepository, times(1)).getCategoryById(1L);
    }
}
package com.warehouse.backend.category.application.mapper;

import com.warehouse.backend.category.application.port.input.CreateCategoryRequest;
import com.warehouse.backend.category.application.port.output.CategoryDto;
import com.warehouse.backend.category.domain.model.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CategoryMapperTest {

    @InjectMocks
    private CategoryMapperImpl categoryMapper;

    @Test
    void testCategoryToCategoryDto() {
        Category category = new Category(1L, "Test Category", "Test Description");
        
        CategoryDto dto = categoryMapper.categoryToCategoryDto(category);
        
        assertThat(dto.getId()).isEqualTo(category.getId());
        assertThat(dto.getName()).isEqualTo(category.getName());
        assertThat(dto.getDescription()).isEqualTo(category.getDescription());
    }

    @Test
    void testCreateCategoryRequestToCategory() {
        CreateCategoryRequest request = new CreateCategoryRequest("Test Category", "Test Description");
        
        Category category = categoryMapper.createCategoryRequestToCategory(request);
        
        assertThat(category.getId()).isNull(); // ID має бути проігнорований
        assertThat(category.getName()).isEqualTo(request.getName());
        assertThat(category.getDescription()).isEqualTo(request.getDescription());
    }
    
    @Test
    void testCategoryToCategoryDto_WithNullInput() {
        Category category = null;
        
        CategoryDto dto = categoryMapper.categoryToCategoryDto(category);
        
        assertThat(dto).isNull();
    }
    
    @Test
    void testCreateCategoryRequestToCategory_WithNullInput() {
        CreateCategoryRequest request = null;
        
        Category category = categoryMapper.createCategoryRequestToCategory(request);
        
        assertThat(category).isNull();
    }
}

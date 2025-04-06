package com.warehouse.backend.category.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.backend.category.application.port.input.CreateCategoryRequest;
import com.warehouse.backend.category.application.port.output.CategoryDto;
import com.warehouse.backend.category.application.service.CategoryService;
import com.warehouse.backend.category.domain.exception.CategoryAlreadyExistsException;
import com.warehouse.backend.category.domain.exception.CategoryNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllCategories() throws Exception {
        Long categoryId = 1L;
        CategoryDto category = new CategoryDto(categoryId, "Test category", "Test description");

        when(categoryService.getAllCategories()).thenReturn(List.of(category));

        mockMvc.perform(get("/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(categoryId))
                .andExpect(jsonPath("$[0].name").value("Test category"))
                .andExpect(jsonPath("$[0].description").value("Test description"));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void shouldReturnCategoryById() throws Exception {
        Long categoryId = 1L;
        CategoryDto category = new CategoryDto(categoryId, "Test category", "Test description");

        when(categoryService.getCategoryById(categoryId)).thenReturn(category);

        mockMvc.perform(get("/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value("Test category"))
                .andExpect(jsonPath("$.description").value("Test description"));

        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    void shouldReturn404WhenCategoryNotFound() throws Exception {
        Long categoryId = 1L;
        when(categoryService.getCategoryById(categoryId)).thenThrow(new CategoryNotFoundException(categoryId));

        mockMvc.perform(get("/category/" + categoryId))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).getCategoryById(categoryId);
    }

    @Test
    void shouldCreateCategory() throws Exception {
        Long categoryId = 1L;
        CreateCategoryRequest request = new CreateCategoryRequest("Test category", "Test description");
        CategoryDto response = new CategoryDto(categoryId, "Test category", "Test description");

        when(categoryService.saveCategory(any(CreateCategoryRequest.class))).thenReturn(response);

        mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.description").value(response.getDescription()));

        verify(categoryService, times(1)).saveCategory(any(CreateCategoryRequest.class));
    }

    @Test
    void shouldReturn409WhenCategoryAlreadyExists() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest("Duplicate", "Test description");

        when(categoryService.saveCategory(any(CreateCategoryRequest.class)))
                .thenThrow(new CategoryAlreadyExistsException("Duplicate"));

        mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        verify(categoryService, times(1)).saveCategory(any(CreateCategoryRequest.class));
    }


    @Test
    void shouldUpdateCategory() throws Exception {
        Long categoryId = 1L;
        CreateCategoryRequest request = new CreateCategoryRequest("Category", "Description");
        CategoryDto response = new CategoryDto(categoryId, "Updated category", "Updated description");

        when(categoryService.updateCategory(any(CreateCategoryRequest.class), eq(categoryId))).thenReturn(response);

        mockMvc.perform(put("/category/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.description").value(response.getDescription()));

        verify(categoryService, times(1)).updateCategory(any(CreateCategoryRequest.class), eq(categoryId));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentCategory() throws Exception {
        Long categoryId = 999L;
        CreateCategoryRequest request = new CreateCategoryRequest("Updated category", "Updated description");

        when(categoryService.updateCategory(any(CreateCategoryRequest.class), eq(categoryId)))
                .thenThrow(new CategoryNotFoundException(categoryId));

        mockMvc.perform(put("/category/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).updateCategory(any(CreateCategoryRequest.class), eq(categoryId));
    }

    @Test
    void shouldReturn409WhenUpdatingCategoryWithExistingName() throws Exception {
        Long categoryId = 1L;
        String existingName = "Existing category";
        CreateCategoryRequest request = new CreateCategoryRequest(existingName, "Updated description");

        when(categoryService.updateCategory(any(CreateCategoryRequest.class), eq(categoryId)))
                .thenThrow(new CategoryAlreadyExistsException(existingName));

        mockMvc.perform(put("/category/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        verify(categoryService, times(1)).updateCategory(any(CreateCategoryRequest.class), eq(categoryId));
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        Long categoryId = 1L;
        doNothing().when(categoryService).deleteCategoryById(categoryId);

        mockMvc.perform(delete("/category/" + categoryId))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategoryById(categoryId);
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentCategory() throws Exception {
        Long categoryId = 999L;
        doThrow(new CategoryNotFoundException(categoryId))
                .when(categoryService).deleteCategoryById(categoryId);

        mockMvc.perform(delete("/category/" + categoryId))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).deleteCategoryById(categoryId);
    }
}
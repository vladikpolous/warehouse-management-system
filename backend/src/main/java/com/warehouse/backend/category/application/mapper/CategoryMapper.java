package com.warehouse.backend.category.application.mapper;


import com.warehouse.backend.category.application.port.input.CreateCategoryRequest;
import com.warehouse.backend.category.application.port.output.CategoryDto;
import com.warehouse.backend.category.domain.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto categoryToCategoryDto(Category category);

    @Mapping(target = "id", ignore = true)
    Category createCategoryRequestToCategory(CreateCategoryRequest request);
}

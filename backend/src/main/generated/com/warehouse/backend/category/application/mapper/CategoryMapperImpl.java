package com.warehouse.backend.category.application.mapper;

import com.warehouse.backend.category.application.port.input.CreateCategoryRequest;
import com.warehouse.backend.category.application.port.output.CategoryDto;
import com.warehouse.backend.category.domain.model.Category;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-19T16:14:03+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryDto categoryToCategoryDto(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId( category.getId() );
        categoryDto.setName( category.getName() );
        categoryDto.setDescription( category.getDescription() );

        return categoryDto;
    }

    @Override
    public Category createCategoryRequestToCategory(CreateCategoryRequest request) {
        if ( request == null ) {
            return null;
        }

        Category.CategoryBuilder category = Category.builder();

        category.name( request.getName() );
        category.description( request.getDescription() );

        return category.build();
    }
}

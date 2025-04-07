package com.warehouse.backend.product.application.mapper;

import com.warehouse.backend.category.application.port.output.CategoryDto;
import com.warehouse.backend.category.domain.model.Category;
import com.warehouse.backend.product.application.port.input.CreateProductRequest;
import com.warehouse.backend.product.application.port.output.ProductDto;
import com.warehouse.backend.product.domain.model.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-07T15:06:48+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto productToProductDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto productDto = new ProductDto();

        productDto.setId( product.getId() );
        productDto.setName( product.getName() );
        productDto.setDescription( product.getDescription() );
        productDto.setCategory( categoryToCategoryDto( product.getCategory() ) );
        productDto.setCreatedDate( product.getCreatedDate() );

        return productDto;
    }

    @Override
    public Product createProductRequestToProduct(CreateProductRequest request) {
        if ( request == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.name( request.getName() );
        product.description( request.getDescription() );

        return product.build();
    }

    protected CategoryDto categoryToCategoryDto(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId( category.getId() );
        categoryDto.setName( category.getName() );
        categoryDto.setDescription( category.getDescription() );

        return categoryDto;
    }
}

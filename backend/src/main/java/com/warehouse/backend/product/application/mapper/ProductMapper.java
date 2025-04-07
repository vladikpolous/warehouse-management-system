package com.warehouse.backend.product.application.mapper;

import com.warehouse.backend.product.application.port.input.CreateProductRequest;
import com.warehouse.backend.product.application.port.output.ProductDto;
import com.warehouse.backend.product.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto productToProductDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    Product createProductRequestToProduct(CreateProductRequest request);
}

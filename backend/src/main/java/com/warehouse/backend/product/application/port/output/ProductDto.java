package com.warehouse.backend.product.application.port.output;

import com.warehouse.backend.category.application.port.output.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements Serializable {
    private Long id;
    private String name;
    private String description;
    private CategoryDto category;
    private LocalDateTime createdDate;
}

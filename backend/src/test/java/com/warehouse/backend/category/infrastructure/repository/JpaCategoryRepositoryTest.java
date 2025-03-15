package com.warehouse.backend.category.infrastructure.repository;

import com.warehouse.backend.category.domain.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaCategoryRepositoryTest {

    @Autowired
    private JpaCategoryRepository jpaCategoryRepository;

    @Test
    void testCreateNewCategoryAndGetCategoryById() {
        Category category = new Category();
        category.setName("Test category");
        category.setDescription("Test description");
        Category savedCategory = jpaCategoryRepository.createNewCategory(category);
        Optional<Category> foundCategory = jpaCategoryRepository.getCategoryById(savedCategory.getId());
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo(category.getName());
        assertThat(foundCategory.get().getDescription()).isEqualTo(category.getDescription());
    }

    @Test
    void testFindAllCategories() {
        var categories = jpaCategoryRepository.findAll();
        assertThat(categories).isEmpty();
    }
}
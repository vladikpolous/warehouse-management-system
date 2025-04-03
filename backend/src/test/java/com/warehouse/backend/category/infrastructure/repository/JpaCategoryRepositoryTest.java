package com.warehouse.backend.category.infrastructure.repository;

import com.warehouse.backend.category.domain.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaCategoryRepositoryTest {

    @Autowired
    private JpaCategoryRepository jpaCategoryRepository;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        jpaCategoryRepository.deleteAll();
        testCategory = new Category();
        testCategory.setName("Test category");
        testCategory.setDescription("Test description");
    }

    @Test
    void testCreateNewCategory() {
        Category savedCategory = jpaCategoryRepository.createNewCategory(testCategory);

        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo(testCategory.getName());
        assertThat(savedCategory.getDescription()).isEqualTo(testCategory.getDescription());
    }

    @Test
    void testGetCategoryById() {
        Category savedCategory = jpaCategoryRepository.createNewCategory(testCategory);

        Optional<Category> foundCategory = jpaCategoryRepository.getCategoryById(savedCategory.getId());

        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo(testCategory.getName());
        assertThat(foundCategory.get().getDescription()).isEqualTo(testCategory.getDescription());
    }

    @Test
    void testGetCategoryById_WhenCategoryDoesNotExist() {
        Optional<Category> foundCategory = jpaCategoryRepository.getCategoryById(999L);

        assertThat(foundCategory).isEmpty();
    }

    @Test
    void testGetAllCategories_WhenNoCategories() {
        List<Category> categories = jpaCategoryRepository.getAllCategories();

        assertThat(categories).isEmpty();
    }

    @Test
    void testGetAllCategories_WhenCategoriesExist() {
        Category savedCategory1 = jpaCategoryRepository.createNewCategory(testCategory);

        Category secondCategory = new Category();
        secondCategory.setName("Second category");
        secondCategory.setDescription("Second description");
        Category savedCategory2 = jpaCategoryRepository.createNewCategory(secondCategory);

        List<Category> categories = jpaCategoryRepository.getAllCategories();

        assertThat(categories).hasSize(2);
        assertThat(categories).extracting(Category::getName)
                .containsExactlyInAnyOrder(savedCategory1.getName(), savedCategory2.getName());
    }

    @Test
    void testDeleteCategoryById() {
        Category savedCategory = jpaCategoryRepository.createNewCategory(testCategory);
        assertThat(jpaCategoryRepository.getAllCategories()).hasSize(1);

        jpaCategoryRepository.deleteCategoryById(savedCategory.getId());

        assertThat(jpaCategoryRepository.getAllCategories()).isEmpty();
        assertThat(jpaCategoryRepository.getCategoryById(savedCategory.getId())).isEmpty();
    }

    @Test
    void testDeleteCategoryById_WhenCategoryDoesNotExist() {
        jpaCategoryRepository.deleteCategoryById(999L);
        assertThat(jpaCategoryRepository.getAllCategories()).isEmpty();
    }
}
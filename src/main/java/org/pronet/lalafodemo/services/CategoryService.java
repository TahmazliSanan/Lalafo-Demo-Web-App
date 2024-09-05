package org.pronet.lalafodemo.services;

import org.pronet.lalafodemo.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryService {
    void createCategory(Category category, MultipartFile file) throws IOException;
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    List<Category> getLastAddedFourCategories();
    Page<Category> getAllCategoriesByName(String name, Integer page, Integer size);
    void updateCategory(Category category, MultipartFile file) throws IOException;
    void deleteCategoryById(Long id);
    Boolean isExistCategoryByName(String name);
}

package org.pronet.lalafodemo.services.implementations;

import org.pronet.lalafodemo.entities.Category;
import org.pronet.lalafodemo.repositories.CategoryRepository;
import org.pronet.lalafodemo.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImplementation implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void createCategory(Category category, MultipartFile file)
            throws IOException {
        Category newCategory = new Category();
        newCategory.setName(category.getName().trim());
        newCategory.setImageName(file.getOriginalFilename());
        categoryRepository.save(newCategory);
        if (!file.isEmpty()) {
            File savedFile = new ClassPathResource("static/").getFile();
            Path path = Paths.get(savedFile.getAbsolutePath() +
                            File.separator +
                            "category-images" +
                            File.separator +
                            file.getOriginalFilename());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getAllCategoriesByName(String name) {
        if (name == null) {
            name = "";
        }
        return categoryRepository.findAllByNameContainingIgnoreCase(name.trim());
    }

    @Override
    public void updateCategory(Category category, MultipartFile file)
            throws IOException {
        Category foundedCategory = getCategoryById(category.getId());
        String imageName = file.isEmpty() ? foundedCategory.getImageName() : file.getOriginalFilename();
        foundedCategory.setName(category.getName().trim());
        foundedCategory.setImageName(imageName);
        foundedCategory.setCreatedDateTime(LocalDateTime.now());
        categoryRepository.save(foundedCategory);
        if (!file.isEmpty()) {
            File savedFile = new ClassPathResource("static/").getFile();
            Path path = Paths.get(
                    savedFile.getAbsolutePath() +
                            File.separator +
                            "category-images" +
                            File.separator +
                            file.getOriginalFilename());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Override
    public void deleteCategoryById(Long id) {
        Category foundedCategory = getCategoryById(id);
        categoryRepository.delete(foundedCategory);
    }

    @Override
    public Boolean isExistCategoryByName(String name) {
        return categoryRepository.existsByNameContainingIgnoreCase(name.trim());
    }
}

package org.pronet.lalafodemo.services.implementations;

import org.pronet.lalafodemo.entities.Category;
import org.pronet.lalafodemo.entities.Product;
import org.pronet.lalafodemo.entities.User;
import org.pronet.lalafodemo.repositories.CategoryRepository;
import org.pronet.lalafodemo.repositories.ProductRepository;
import org.pronet.lalafodemo.repositories.UserRepository;
import org.pronet.lalafodemo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class ProductServiceImplementation implements ProductService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createProduct(Product product, MultipartFile file, Long categoryId, Long userId)
            throws IOException {
        Optional<Category> foundedCategory = categoryRepository.findById(categoryId);
        Optional<User> foundedUser = userRepository.findById(userId);
        if (foundedCategory.isPresent() && foundedUser.isPresent()) {
            Category existCategory = foundedCategory.get();
            User existUser = foundedUser.get();
            Product newProduct = new Product();
            newProduct.setName(product.getName().trim());
            newProduct.setStatus(product.getStatus().trim());
            newProduct.setLocation(product.getLocation().trim());
            newProduct.setMobileNumber(product.getMobileNumber().trim());
            newProduct.setImageName(file.getOriginalFilename());
            newProduct.setPrice(product.getPrice());
            newProduct.setCategory(existCategory);
            newProduct.setUser(existUser);
            productRepository.save(newProduct);
            if (!file.isEmpty()) {
                File savedFile = new ClassPathResource("static/").getFile();
                Path path = Paths.get(savedFile.getAbsolutePath() +
                                File.separator +
                                "product-images" +
                                File.separator +
                                file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public Page<Product> getAllProductsByCategoryId(Long categoryId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAllByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<Product> getAllProductsByNameAndUserId(String character, Long userId, Integer page, Integer size) {
        if (character == null) {
            character = "";
        }
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAllByNameContainingIgnoreCaseAndUserId(character.trim(), userId, pageable);
    }

    @Override
    public Page<Product> filterAllProductsByPriceNameAndStatus(
            Double minimumPrice, Double maximumPrice, String character,
            String status, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        if (minimumPrice == null) {
            minimumPrice = 0.0;
        }
        if (maximumPrice == null) {
            maximumPrice = Double.MAX_VALUE;
        }
        if (character == null) {
            character = "";
        }
        if (!StringUtils.hasText(status) || status.equalsIgnoreCase("Hamısı")) {
            return productRepository.findAllByPriceBetweenAndNameContainingIgnoreCase(minimumPrice, maximumPrice, character.trim(), pageable);
        } else {
            return productRepository.findAllByPriceBetweenAndNameContainingIgnoreCaseAndStatus(minimumPrice, maximumPrice, character.trim(), status, pageable);
        }
    }

    @Override
    public void updateProduct(Product product, MultipartFile file, Long categoryId) throws IOException {
        Optional<Category> foundedCategory = categoryRepository.findById(categoryId);
        Product foundedProduct = getProductById(product.getId());
        String imageName = file.isEmpty() ? foundedProduct.getImageName() : file.getOriginalFilename();
        if (foundedCategory.isPresent()) {
            Category category = foundedCategory.get();
            foundedProduct.setName(product.getName().trim());
            foundedProduct.setStatus(product.getStatus().trim());
            foundedProduct.setLocation(product.getLocation().trim());
            foundedProduct.setMobileNumber(product.getMobileNumber().trim());
            foundedProduct.setImageName(imageName);
            foundedProduct.setPrice(product.getPrice());
            foundedProduct.setCategory(category);
            productRepository.save(foundedProduct);
            if (!file.isEmpty()) {
                File savedFile = new ClassPathResource("static/").getFile();
                Path path = Paths.get(savedFile.getAbsolutePath() +
                                File.separator +
                                "product-images" +
                                File.separator +
                                file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    @Override
    public void deleteProductById(Long id) {
        Product foundedProduct = getProductById(id);
        productRepository.delete(foundedProduct);
    }
}

package org.pronet.lalafodemo.services;

import org.pronet.lalafodemo.entities.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    void createProduct(Product product, MultipartFile file, Long categoryId) throws IOException;
    Product getProductById(Long id);
    List<Product> getAllProductsByCategoryId(Long categoryId);
    List<Product> filterAllProductsByPriceAndName(Double minimumPrice, Double maximumPrice, String character);
    void updateProduct(Product product, MultipartFile file, Long categoryId) throws IOException;
    void deleteProductById(Long id);
}

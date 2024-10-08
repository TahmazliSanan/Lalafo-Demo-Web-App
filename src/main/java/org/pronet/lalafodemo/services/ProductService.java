package org.pronet.lalafodemo.services;

import org.pronet.lalafodemo.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    void createProduct(Product product, MultipartFile file, Long categoryId, Long userId) throws IOException;
    Product getProductById(Long id);
    List<Product> getLastAddedFourProducts();
    Page<Product> getAllProductsByCategoryId(Long categoryId, Integer page, Integer size);
    Page<Product> getAllProductsByNameAndUserId(String character, Long userId, Integer page, Integer size);
    Page<Product> filterAllProductsByPriceNameAndStatus(Double minimumPrice, Double maximumPrice, String character, String status, Integer page, Integer size);
    void updateProduct(Product product, MultipartFile file, Long categoryId) throws IOException;
    void deleteProductById(Long id);
}

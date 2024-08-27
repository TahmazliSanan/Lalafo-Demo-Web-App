package org.pronet.lalafodemo.services;

import org.pronet.lalafodemo.entities.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    void createProduct(Product product, MultipartFile file, Long categoryId) throws IOException;
    Product getProductById(Long id);
    List<Product> getAllProducts();
    List<Product> getAllProductsByName(String name);
    void updateProduct(Product product, MultipartFile file, Long categoryId) throws IOException;
    void deleteProductById(Long id);
}

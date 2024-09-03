package org.pronet.lalafodemo.repositories;

import org.pronet.lalafodemo.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> findAllByNameContainingIgnoreCaseAndUserId(String character, Long userId, Pageable pageable);
    Page<Product> findAllByPriceBetweenAndNameContainingIgnoreCase(Double minimumPrice, Double maximumPrice, String character, Pageable pageable);
    Page<Product> findAllByPriceBetweenAndNameContainingIgnoreCaseAndStatus(Double minimumPrice, Double maximumPrice, String character, String status, Pageable pageable);
}

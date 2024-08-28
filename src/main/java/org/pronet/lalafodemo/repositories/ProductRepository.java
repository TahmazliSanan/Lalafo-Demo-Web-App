package org.pronet.lalafodemo.repositories;

import org.pronet.lalafodemo.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCategoryId(Long categoryId);
    List<Product> findAllByPriceBetweenAndNameContainingIgnoreCase(Double minimumPrice, Double maximumPrice, String character);
}

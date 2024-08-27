package org.pronet.lalafodemo.repositories;

import org.pronet.lalafodemo.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByNameContainingIgnoreCase(String name);
    List<Product> findAllByCategoryId(Long categoryId);
}

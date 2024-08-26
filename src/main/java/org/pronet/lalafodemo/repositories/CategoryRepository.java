package org.pronet.lalafodemo.repositories;

import org.pronet.lalafodemo.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByNameContainingIgnoreCase(String name);
    Boolean existsByNameContainingIgnoreCase(String name);
}

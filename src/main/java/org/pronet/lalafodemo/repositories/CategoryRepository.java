package org.pronet.lalafodemo.repositories;

import org.pronet.lalafodemo.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Boolean existsByNameContainingIgnoreCase(String name);
}

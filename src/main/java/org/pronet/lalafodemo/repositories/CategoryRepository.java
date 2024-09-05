package org.pronet.lalafodemo.repositories;

import org.pronet.lalafodemo.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findTop4ByOrderByCreatedDateTimeDesc();
    Page<Category> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
    Boolean existsByNameContainingIgnoreCase(String name);
}

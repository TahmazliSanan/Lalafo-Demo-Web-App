package org.pronet.lalafodemo.repositories;

import org.pronet.lalafodemo.entities.FavoriteList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteListRepository extends JpaRepository<FavoriteList, Long> {
    FavoriteList findByUserIdAndProductId(Long userId, Long productId);

    @Query("SELECT f FROM FavoriteList f " +
            "JOIN f.product p " +
            "WHERE f.user.id = :userId " +
            "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:minimumPrice IS NULL OR p.price >= :minimumPrice) " +
            "AND (:maximumPrice IS NULL OR p.price <= :maximumPrice)")
    Page<FavoriteList> findAllFavoriteListByFilters(
            @Param(value = "userId") Long userId,
            @Param(value = "name") String name,
            @Param(value = "minimumPrice") Double minimumPrice,
            @Param(value = "maximumPrice") Double maximumPrice,
            Pageable pageable);

    @Query("SELECT f FROM FavoriteList f " +
            "JOIN f.product p " +
            "WHERE f.user.id = :userId " +
            "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:minimumPrice IS NULL OR p.price >= :minimumPrice) " +
            "AND (:maximumPrice IS NULL OR p.price <= :maximumPrice) " +
            "AND (:status IS NULL OR p.status = :status)")
    Page<FavoriteList> findAllFavoriteListByFiltersUsingStatus(
            @Param(value = "userId") Long userId,
            @Param(value = "name") String name,
            @Param(value = "minimumPrice") Double minimumPrice,
            @Param(value = "maximumPrice") Double maximumPrice,
            @Param(value = "status") String status,
            Pageable pageable);

    void deleteByUserIdAndProductId(Long userId, Long productId);
}

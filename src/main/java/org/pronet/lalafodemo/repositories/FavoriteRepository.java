package org.pronet.lalafodemo.repositories;

import org.pronet.lalafodemo.entities.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByUserId(Long userId);
    Favorite findByUserIdAndProductId(Long userId, Long productId);
}

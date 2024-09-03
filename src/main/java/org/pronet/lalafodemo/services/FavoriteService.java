package org.pronet.lalafodemo.services;

import org.pronet.lalafodemo.entities.Favorite;
import org.springframework.data.domain.Page;

public interface FavoriteService {
    void addProductToFavoriteList(Long userId, Long productId);
    Page<Favorite> filterFavoriteList(Long userId, String name, Double minimumPrice, Double maximumPrice, String status, Integer page, Integer size);
    Boolean isProductInFavoriteList(Long userId, Long productId);
}

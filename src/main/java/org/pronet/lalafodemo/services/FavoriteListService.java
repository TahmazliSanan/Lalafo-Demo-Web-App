package org.pronet.lalafodemo.services;

import org.pronet.lalafodemo.entities.FavoriteList;
import org.springframework.data.domain.Page;

public interface FavoriteListService {
    void addProductToFavoriteList(Long userId, Long productId);
    void removeProductFromFavoriteList(Long userId, Long productId);
    Page<FavoriteList> filterFavoriteList(Long userId, String name, Double minimumPrice, Double maximumPrice, String status, Integer page, Integer size);
    Boolean isProductInFavoriteList(Long userId, Long productId);
}

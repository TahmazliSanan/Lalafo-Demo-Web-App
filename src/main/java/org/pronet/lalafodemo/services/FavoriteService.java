package org.pronet.lalafodemo.services;

import org.pronet.lalafodemo.entities.Favorite;

import java.util.List;

public interface FavoriteService {
    void addProductToFavoriteList(Long userId, Long productId);
    List<Favorite> getFavoriteListByUserId(Long userId);
    Boolean isProductInFavoriteList(Long userId, Long productId);
}

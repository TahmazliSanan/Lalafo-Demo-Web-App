package org.pronet.lalafodemo.services.implementations;

import org.pronet.lalafodemo.entities.Favorite;
import org.pronet.lalafodemo.entities.Product;
import org.pronet.lalafodemo.entities.User;
import org.pronet.lalafodemo.repositories.FavoriteRepository;
import org.pronet.lalafodemo.repositories.ProductRepository;
import org.pronet.lalafodemo.repositories.UserRepository;
import org.pronet.lalafodemo.services.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteServiceImplementation implements FavoriteService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Override
    public void addProductToFavoriteList(Long userId, Long productId) {
        Optional<User> foundedUser = userRepository.findById(userId);
        Optional<Product> foundedProduct = productRepository.findById(productId);
        if (foundedUser.isPresent() && foundedProduct.isPresent()) {
            User user = foundedUser.get();
            Product product = foundedProduct.get();
            Favorite foundedFavoriteList = favoriteRepository.findByUserIdAndProductId(userId, productId);
            if (foundedFavoriteList == null) {
                Favorite favorite = new Favorite();
                favorite.setUser(user);
                favorite.setProduct(product);
                favoriteRepository.save(favorite);
            }
        }
    }

    @Override
    public List<Favorite> getFavoriteListByUserId(Long userId) {
        return favoriteRepository.findAllByUserId(userId);
    }

    @Override
    public Boolean isProductInFavoriteList(Long userId, Long productId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Product> productOptional = productRepository.findById(productId);
        if (userOptional.isPresent() && productOptional.isPresent()) {
            Favorite foundedFavorite = favoriteRepository.findByUserIdAndProductId(userId, productId);
            return foundedFavorite != null;
        }
        return false;
    }
}

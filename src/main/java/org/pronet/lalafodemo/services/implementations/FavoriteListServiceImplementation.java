package org.pronet.lalafodemo.services.implementations;

import org.pronet.lalafodemo.entities.FavoriteList;
import org.pronet.lalafodemo.entities.Product;
import org.pronet.lalafodemo.entities.User;
import org.pronet.lalafodemo.repositories.FavoriteListRepository;
import org.pronet.lalafodemo.repositories.ProductRepository;
import org.pronet.lalafodemo.repositories.UserRepository;
import org.pronet.lalafodemo.services.FavoriteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class FavoriteListServiceImplementation implements FavoriteListService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FavoriteListRepository favoriteListRepository;

    @Override
    public void addProductToFavoriteList(Long userId, Long productId) {
        Optional<User> foundedUser = userRepository.findById(userId);
        Optional<Product> foundedProduct = productRepository.findById(productId);
        if (foundedUser.isPresent() && foundedProduct.isPresent()) {
            User user = foundedUser.get();
            Product product = foundedProduct.get();
            FavoriteList foundedFavoriteListList = favoriteListRepository.findByUserIdAndProductId(userId, productId);
            if (foundedFavoriteListList == null) {
                FavoriteList favoriteList = new FavoriteList();
                favoriteList.setUser(user);
                favoriteList.setProduct(product);
                favoriteListRepository.save(favoriteList);
            }
        }
    }

    @Override
    @Transactional
    public void removeProductFromFavoriteList(Long userId, Long productId) {
        Optional<User> foundedUser = userRepository.findById(userId);
        Optional<Product> foundedProduct = productRepository.findById(productId);
        if (foundedUser.isPresent() && foundedProduct.isPresent()) {
            favoriteListRepository.deleteByUserIdAndProductId(userId, productId);
        }
    }

    @Override
    public Page<FavoriteList> filterFavoriteList(Long userId, String name, Double minimumPrice, Double maximumPrice, String status, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        if (minimumPrice == null) {
            minimumPrice = 0.0;
        }
        if (maximumPrice == null) {
            maximumPrice = Double.MAX_VALUE;
        }
        if (name == null) {
            name = "";
        }
        if (!StringUtils.hasText(status) || status.equalsIgnoreCase("Hamısı")) {
            return favoriteListRepository.findAllFavoriteListByFilters(userId, name.trim(), minimumPrice, maximumPrice, pageable);
        } else {
            return favoriteListRepository.findAllFavoriteListByFiltersUsingStatus(userId, name.trim(), minimumPrice, maximumPrice, status, pageable);
        }
    }

    @Override
    public Boolean isProductInFavoriteList(Long userId, Long productId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Product> productOptional = productRepository.findById(productId);
        if (userOptional.isPresent() && productOptional.isPresent()) {
            FavoriteList foundedFavoriteList = favoriteListRepository.findByUserIdAndProductId(userId, productId);
            return foundedFavoriteList != null;
        }
        return false;
    }
}

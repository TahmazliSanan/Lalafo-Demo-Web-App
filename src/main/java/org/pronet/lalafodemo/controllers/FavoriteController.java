package org.pronet.lalafodemo.controllers;

import jakarta.servlet.http.HttpSession;
import org.pronet.lalafodemo.entities.FavoriteList;
import org.pronet.lalafodemo.entities.User;
import org.pronet.lalafodemo.services.FavoriteListService;
import org.pronet.lalafodemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping(value = "/favorite-list")
public class FavoriteController {
    @Autowired
    private FavoriteListService favoriteListService;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public User getLoggedInUserDetails(
            Principal principal,
            Model model) {
        if (principal != null) {
            String email = principal.getName();
            User foundedUser = userService.getUserByEmail(email);
            model.addAttribute("foundedUser", foundedUser);
            return foundedUser;
        } else {
            return null;
        }
    }

    private User getLoggedInUserDetails(Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            return userService.getUserByEmail(email);
        } else {
            return null;
        }
    }

    @GetMapping(value = "/view")
    public String favoriteListView(
            @RequestParam(value = "minimumPrice", required = false) Double minimumPrice,
            @RequestParam(value = "maximumPrice", required = false) Double maximumPrice,
            @RequestParam(value = "character", required = false) String character,
            @RequestParam(value = "status", required = false, defaultValue = "Hamısı") String status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "50") Integer size,
            Principal principal,
            Model model) {
        User loggedInUser = getLoggedInUserDetails(principal);
        Page<FavoriteList> favoriteList = favoriteListService.filterFavoriteList(loggedInUser.getId(), character, minimumPrice, maximumPrice, status, page, size);
        model.addAttribute("minimumPrice", minimumPrice);
        model.addAttribute("maximumPrice", maximumPrice);
        model.addAttribute("character", character);
        model.addAttribute("status", status);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("favoriteList", favoriteList);
        return "favorite-list/product-list";
    }

    @PostMapping(value = "/add-to-favorites")
    public String addProductToFavoriteList(
            @RequestParam(value = "productId") Long productId,
            Principal principal,
            HttpSession session) {
        User loggedInUser = getLoggedInUserDetails(principal);
        favoriteListService.addProductToFavoriteList(loggedInUser.getId(), productId);
        session.setAttribute("successMessage", "Məhsul seçilmişlər siyahısına uğurla əlavə olundu!");
        return "redirect:/product/details/" + productId;
    }

    @PostMapping(value = "/remove-from-favorites")
    public String removeFromFavorites(
            @RequestParam(value = "productId") Long productId,
            Principal principal,
            HttpSession session) {
        User loggedInUser = getLoggedInUserDetails(principal);
        favoriteListService.removeProductFromFavoriteList(loggedInUser.getId(), productId);
        session.setAttribute("successMessage", "Məhsul seçilmişlər siyahısından uğurla silindi!");
        return "redirect:/product/details/" + productId;
    }

    @PostMapping(value = "/remove-from-favorites-same-view")
    public String removeFromFavoritesSameView(
            @RequestParam(value = "productId") Long productId,
            Principal principal,
            HttpSession session) {
        User loggedInUser = getLoggedInUserDetails(principal);
        favoriteListService.removeProductFromFavoriteList(loggedInUser.getId(), productId);
        session.setAttribute("successMessage", "Məhsul seçilmişlər siyahısından uğurla silindi!");
        return "redirect:/favorite-list/view";
    }
}

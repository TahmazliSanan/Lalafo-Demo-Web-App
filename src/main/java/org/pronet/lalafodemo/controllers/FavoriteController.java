package org.pronet.lalafodemo.controllers;

import jakarta.servlet.http.HttpSession;
import org.pronet.lalafodemo.entities.Favorite;
import org.pronet.lalafodemo.entities.User;
import org.pronet.lalafodemo.services.FavoriteService;
import org.pronet.lalafodemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/favorite-list")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

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
            Principal principal,
            Model model) {
        User loggedInUser = getLoggedInUserDetails(principal);
        List<Favorite> favoriteList = favoriteService.getFavoriteListByUserId(loggedInUser.getId());
        model.addAttribute("favoriteList", favoriteList);
        return "favorite-list/product-list";
    }

    @PostMapping(value = "/add-to-favorites")
    public String addProductToFavoriteList(
            @RequestParam(value = "productId") Long productId,
            Principal principal,
            HttpSession session) {
        User loggedInUser = getLoggedInUserDetails(principal);
        favoriteService.addProductToFavoriteList(loggedInUser.getId(), productId);
        session.setAttribute("successMessage", "Məhsul seçilmişlər siyahısına uğurla əlavə olundu!");
        return "redirect:/product/details/" + productId;
    }
}

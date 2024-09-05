package org.pronet.lalafodemo.controllers;

import org.pronet.lalafodemo.entities.Category;
import org.pronet.lalafodemo.entities.Product;
import org.pronet.lalafodemo.entities.User;
import org.pronet.lalafodemo.services.CategoryService;
import org.pronet.lalafodemo.services.ProductService;
import org.pronet.lalafodemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @ModelAttribute
    public void getLoggedInUserDetails(
            Principal principal,
            Model model) {
        if (principal != null) {
            String email = principal.getName();
            User foundedUser = userService.getUserByEmail(email);
            model.addAttribute("foundedUser", foundedUser);
        } else {
            model.addAttribute("foundedUser", null);
        }
    }

    @GetMapping(value = "/")
    public String homeView(Model model) {
        List<Category> lastAddedFourCategories = categoryService.getLastAddedFourCategories();
        List<Product> lastAddedFourProducts = productService.getLastAddedFourProducts();
        model.addAttribute("lastAddedFourCategories", lastAddedFourCategories);
        model.addAttribute("lastAddedFourProducts", lastAddedFourProducts);
        return "home/home";
    }
}

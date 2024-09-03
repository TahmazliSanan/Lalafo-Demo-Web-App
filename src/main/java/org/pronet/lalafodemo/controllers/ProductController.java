package org.pronet.lalafodemo.controllers;

import jakarta.servlet.http.HttpSession;
import org.pronet.lalafodemo.entities.Category;
import org.pronet.lalafodemo.entities.Product;
import org.pronet.lalafodemo.entities.User;
import org.pronet.lalafodemo.services.CategoryService;
import org.pronet.lalafodemo.services.FavoriteService;
import org.pronet.lalafodemo.services.ProductService;
import org.pronet.lalafodemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private FavoriteService favoriteService;

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

    @GetMapping(value = "/create-view")
    public String createProductView(Model model) {
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        return "product/create-product";
    }

    @PostMapping(value = "/create")
    public String createProduct(
            @RequestParam(value = "categoryId") Long categoryId,
            @ModelAttribute(value = "product") Product product,
            @RequestParam(value = "file") MultipartFile file,
            Principal principal,
            HttpSession session) throws IOException {
        User loggedInUser = getLoggedInUserDetails(principal);
        productService.createProduct(product, file, categoryId, loggedInUser.getId());
        session.setAttribute("successMessage", "Məhsul uğurla paylaşıldı!");
        return "redirect:/product/create-view";
    }

    @GetMapping(value = "/details/{id}")
    public String productDetailsView(
            @PathVariable(value = "id") Long id,
            Principal principal,
            Model model) {
        User loggedInUser = getLoggedInUserDetails(principal);
        Product foundedProduct = productService.getProductById(id);
        Boolean isExistProductInFavoriteList = favoriteService.isProductInFavoriteList(loggedInUser.getId(), id);
        System.out.println("STATUS: " + isExistProductInFavoriteList);
        model.addAttribute("foundedProduct", foundedProduct);
        model.addAttribute("isExistProductInFavoriteList", isExistProductInFavoriteList);
        return "product/product-details";
    }

    @GetMapping(value = "/list")
    public String productListView(
            @RequestParam(value = "minimumPrice", required = false) Double minimumPrice,
            @RequestParam(value = "maximumPrice", required = false) Double maximumPrice,
            @RequestParam(value = "character", required = false) String character,
            @RequestParam(value = "status", required = false, defaultValue = "Hamısı") String status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "50") Integer size,
            Model model) {
        Page<Product> productList = productService.filterAllProductsByPriceNameAndStatus(minimumPrice, maximumPrice, character, status, page, size);
        model.addAttribute("minimumPrice", minimumPrice);
        model.addAttribute("maximumPrice", maximumPrice);
        model.addAttribute("character", character);
        model.addAttribute("status", status);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("productList", productList);
        return "product/product-list";
    }

    @GetMapping(value = "/list/my-products")
    public String myProductListView(
            @RequestParam(value = "character", required = false) String character,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            Principal principal,
            Model model) {
        User loggedInUser = getLoggedInUserDetails(principal);
        Page<Product> productList = productService.getAllProductsByNameAndUserId(character, loggedInUser.getId(), page, size);
        model.addAttribute("character", character);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("productList", productList);
        return "product/my-product-list";
    }

    @GetMapping(value = "/update-view/{id}")
    public String updateProductView(
            @PathVariable(value = "id") Long id,
            Model model,
            HttpSession session) {
        List<Category> categoryList = categoryService.getAllCategories();
        Product foundedProduct = productService.getProductById(id);
        if (foundedProduct == null) {
            session.setAttribute("errorMessage", "Məhsul mövcud deyil. " +
                    "Zəhmət olmasa mövcud məhsulu düzənləməyə cəhd edin!");
            return "redirect:/product/list";
        } else {
            model.addAttribute("foundedProduct", foundedProduct);
            model.addAttribute("categoryList", categoryList);
            return "product/update-product";
        }
    }

    @PostMapping(value = "/update")
    public String updateProduct(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "categoryId") Long categoryId,
            @ModelAttribute(value = "product") Product product,
            @RequestParam(value = "file") MultipartFile file,
            HttpSession session) throws IOException {
        productService.updateProduct(product, file, categoryId);
        session.setAttribute("successMessage", "Məhsul uğurla yadda saxlanıldı!");
        return "redirect:/product/update-view/" + id;
    }

    @GetMapping(value = "/delete-view/{id}")
    public String deleteProductView(
            @PathVariable(value = "id") Long id,
            Model model,
            HttpSession session) {
        List<Category> categoryList = categoryService.getAllCategories();
        Product foundedProduct = productService.getProductById(id);
        if (foundedProduct == null) {
            session.setAttribute("errorMessage", "Məhsul mövcud deyil. " +
                    "Zəhmət olmasa mövcud məhsulu silməyə cəhd edin!");
            return "redirect:/product/list";
        } else {
            model.addAttribute("foundedProduct", foundedProduct);
            model.addAttribute("categoryList", categoryList);
            return "product/delete-product";
        }
    }

    @PostMapping(value = "/delete")
    public String deleteProduct(
            @RequestParam(value = "id") Long id,
            HttpSession session) {
        productService.deleteProductById(id);
        session.setAttribute("successMessage", "Elan uğurla silindi!");
        return "redirect:/product/list/my-products";
    }
}

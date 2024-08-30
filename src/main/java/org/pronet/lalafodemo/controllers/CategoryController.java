package org.pronet.lalafodemo.controllers;

import jakarta.servlet.http.HttpSession;
import org.pronet.lalafodemo.entities.Category;
import org.pronet.lalafodemo.entities.Product;
import org.pronet.lalafodemo.entities.User;
import org.pronet.lalafodemo.services.CategoryService;
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

@Controller
@RequestMapping(value = "/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

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

    @GetMapping(value = "/create-view")
    public String createCategoryView() {
        return "category/create-category";
    }

    @PostMapping(value = "/create")
    public String createCategory(
            @ModelAttribute(value = "category") Category category,
            @RequestParam(value = "file") MultipartFile file,
            HttpSession session) throws IOException {
        Boolean isExistCategoryByName = categoryService.isExistCategoryByName(category.getName());
        if (isExistCategoryByName) {
            session.setAttribute("errorMessage", "Kateqoriya artıq mövcuddur. " +
                    "Fərqli kateqoriya əlavə etməyə cəhd edin!");
        } else {
            categoryService.createCategory(category, file);
            session.setAttribute("successMessage", "Kateqoriya uğurla əlavə edildi!");
        }
        return "redirect:/category/create-view";
    }

    @GetMapping(value = "/list-for-admin")
    public String categoryListForAdminView(
            @RequestParam(value = "character", required = false) String character,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            Model model) {
        Page<Category> categoryList = categoryService.getAllCategoriesByName(character, page, size);
        model.addAttribute("character", character);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("categoryList", categoryList);
        return "category/category-list-for-admin";
    }

    @GetMapping(value = "/list-for-user")
    public String categoryListForUserView(
            @RequestParam(value = "character", required = false) String character,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            Model model) {
        Page<Category> categoryList = categoryService.getAllCategoriesByName(character, page, size);
        model.addAttribute("character", character);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("categoryList", categoryList);
        return "category/category-list-for-user";
    }

    @GetMapping(value = "/update-view/{id}")
    public String updateCategoryView(
            @PathVariable(value = "id") Long id,
            Model model,
            HttpSession session) {
        Category foundedCategory = categoryService.getCategoryById(id);
        if (foundedCategory == null) {
            session.setAttribute("errorMessage", "Kateqoriya mövcud deyil. " +
                    "Zəhmət olmasa mövcud kateqoriyanı düzənləməyə cəhd edin!");
            return "redirect:/category/list-for-admin";
        } else {
            model.addAttribute("foundedCategory", foundedCategory);
            return "category/update-category";
        }
    }

    @PostMapping(value = "/update")
    public String updateCategory(
            @RequestParam(value = "id") Long id,
            @ModelAttribute(value = "category") Category category,
            @RequestParam(value = "file") MultipartFile file,
            HttpSession session) throws IOException {
        categoryService.updateCategory(category, file);
        session.setAttribute("successMessage", "Kateqoriya uğurla yadda saxlanıldı!");
        return "redirect:/category/update-view/" + id;
    }

    @GetMapping(value = "/delete-view/{id}")
    public String deleteCategoryView(
            @PathVariable(value = "id") Long id,
            Model model,
            HttpSession session) {
        Category foundedCategory = categoryService.getCategoryById(id);
        if (foundedCategory == null) {
            session.setAttribute("errorMessage", "Kateqoriya mövcud deyil. " +
                    "Zəhmət olmasa mövcud kateqoriyanı silməyə cəhd edin!");
            return "redirect:/category/list-for-admin";
        } else {
            model.addAttribute("foundedCategory", foundedCategory);
            return "category/delete-category";
        }
    }

    @PostMapping(value = "/delete")
    public String deleteCategory(
            @RequestParam(value = "id") Long id,
            HttpSession session) {
        categoryService.deleteCategoryById(id);
        session.setAttribute("successMessage", "Kateqoriya uğurla silindi!");
        return "redirect:/category/list-for-admin";
    }

    @GetMapping(value = "/{id}/product-list")
    public String productListByCategoryView(
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "50") Integer size,
            Model model,
            HttpSession session) {
        Category foundedCategory = categoryService.getCategoryById(id);
        if (foundedCategory == null) {
            session.setAttribute("errorMessage", "Kateqoriya mövcud deyil. " +
                    "Zəhmət olmasa mövcud kateqoriya üzrə məhsullara baxmağa cəhd edin!");
            return "redirect:/category/list-for-user";
        } else {
            Page<Product> productList = productService.getAllProductsByCategoryId(id, page, size);
            model.addAttribute("id", id);
            model.addAttribute("page", page);
            model.addAttribute("size", size);
            model.addAttribute("productList", productList);
            return "product/product-list-by-category";
        }
    }
}

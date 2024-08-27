package org.pronet.lalafodemo.controllers;

import jakarta.servlet.http.HttpSession;
import org.pronet.lalafodemo.entities.Category;
import org.pronet.lalafodemo.entities.Product;
import org.pronet.lalafodemo.services.CategoryService;
import org.pronet.lalafodemo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

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
            HttpSession session) throws IOException {
        productService.createProduct(product, file, categoryId);
        session.setAttribute("successMessage", "Məhsul uğurla paylaşıldı!");
        return "redirect:/product/create-view";
    }

    @GetMapping(value = "/details/{id}")
    public String productDetailsView(
            @PathVariable(value = "id") Long id,
            Model model) {
        Product foundedProduct = productService.getProductById(id);
        model.addAttribute("foundedProduct", foundedProduct);
        return "product/product-details";
    }

    @GetMapping(value = "/list")
    public String productListView(
            @RequestParam(value = "character", defaultValue = "") String character,
            Model model) {
        List<Product> productList;
        if (!character.isEmpty()) {
            productList = productService.getAllProductsByName(character);
        } else {
            productList = productService.getAllProducts();
        }
        model.addAttribute("productList", productList);
        return "product/product-list";
    }

    @GetMapping(value = "/list/by-category/{id}")
    public String productListByCategoryView(
            @PathVariable(value = "id") Long id,
            Model model) {
        List<Product> productList = productService.getAllProductsByCategoryId(id);
        model.addAttribute("productList", productList);
        return "product/product-list-by-category";
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
        return "redirect:/product/list";
    }
}

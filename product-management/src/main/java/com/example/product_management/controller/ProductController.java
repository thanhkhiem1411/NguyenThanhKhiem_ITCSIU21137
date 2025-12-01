package com.example.product_management.controller;

import com.example.product_management.entity.Product;
import com.example.product_management.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // List all products - GET /products
    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product-list";
    }

    // Show new product form - GET /products/new
    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    // Show edit form - GET /products/edit/{id}
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "product-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Product not found");
                    return "redirect:/products";
                });
    }

    // Save product - POST /products/save
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              RedirectAttributes redirectAttributes) {

        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("message", "Product saved successfully!");
        return "redirect:/products";
    }

    // Delete product - GET /products/delete/{id}
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {

        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
        return "redirect:/products";
    }

    // Search products - GET /products/search
    @GetMapping("/search")
    public String searchProducts(@RequestParam("keyword") String keyword,
                                 Model model) {

        List<Product> products = productService.searchProducts(keyword);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "product-list";
    }
}


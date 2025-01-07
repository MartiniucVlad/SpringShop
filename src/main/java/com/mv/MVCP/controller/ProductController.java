package com.mv.MVCP.controller;

import com.mv.MVCP.Service.ProductService;
import com.mv.MVCP.Service.UserService;
import com.mv.MVCP.dto.ProductDto;
import com.mv.MVCP.models.Product;
import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.security.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;


    @GetMapping("/test")
    public String test() {
        return "test";
    }


    public void addUserToModel (Model model) {
        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.findByUser(username);
        }
        model.addAttribute("user", user);
    }

    @GetMapping("/products")
    public String products(Model model) {

        List<ProductDto> products = productService.findAll();
        model.addAttribute("products", products);
        addUserToModel(model);
      // Product testProd = new Product("Test Product", "https://pl.nice-cdn.com/upload/image/product/large/default/toy-place-bear-100cm-1-st-819856-en.jpg", "This is a test product");
       // productService.insertProduct(testProd);

        return "product-list";
    }


    @GetMapping("/products/{id}")
    public String productDetails(@PathVariable("id") Long id, Model model) {
        ProductDto productDto = productService.findById(id);
        if (productDto == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", productDto);
        addUserToModel(model);
        return "product-details";
    }

    @GetMapping("/products/create")
    public String createProductForm(Model model) {
         ProductDto productDto = new ProductDto();
         model.addAttribute("productDto", productDto);
        return "product-create";
    }


    @PostMapping("/products/create")
    public String createProduct(@Valid @ModelAttribute("productDto") ProductDto productDto,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "product-create";
        }
        productService.insertProduct(productDto);
        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        var roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        System.out.println("User Roles: " + roles); // Debug: Print user roles
        productService.deleteProduct(id);
        return "redirect:/products";
    }

        @GetMapping("products/edit/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
        ProductDto productDto = productService.findById(id);
        if (productDto == null) {
            return "redirect:/products";
        }
        model.addAttribute("productDto", productDto);
        model.addAttribute("prodId", id);
        return "product-edit";
    }

    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Long id,
                              @Valid @ModelAttribute("productDto") ProductDto productDto,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "product-edit";
        }
        productService.insertProduct(productDto);
        return "redirect:/products";
    }


    @GetMapping("/products/search")
    public String searchProducts(@RequestParam String query, Model model) {
        List<ProductDto> products = productService.searchProducts(query);
        model.addAttribute("products", products);
        return "product-list";
    }


}

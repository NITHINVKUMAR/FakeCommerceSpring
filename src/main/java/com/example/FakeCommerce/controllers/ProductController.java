package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.dto.CreateProductRequestDto;
import com.example.FakeCommerce.schema.Product;
import com.example.FakeCommerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<Product>getAllProducts(){
        return productService.getAllProducts();
    }

    @PostMapping("/api/v1/:id")
    public Product getProductBy(Long id){
        return productService.getProductById(id);
    }

    @PostMapping
    public Product saveProduct(@RequestBody CreateProductRequestDto createProduct){
        return productService.saveProduct(createProduct);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        productService.getProductById(id);
    }

    @GetMapping("/search")
    public List<Product> getProductsByCategory(@RequestParam("categoryName") String category){
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/categories")
    public List<String>getAllCatagories(){
        return productService.getAllCatagories();
    }
}

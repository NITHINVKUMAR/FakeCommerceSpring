package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.dto.CreateProductRequestDto;
import com.example.FakeCommerce.dto.GetProductResponseDto;
import com.example.FakeCommerce.dto.GetProductWithResponseDto;
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
    public List<GetProductResponseDto>getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public GetProductResponseDto getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping("/{id}/detials")
    public GetProductWithResponseDto getProductDetialsById(@PathVariable Long id){
        return productService.getProductDetialsById(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody CreateProductRequestDto createProduct){
        return productService.createProduct(createProduct);
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

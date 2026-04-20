package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.dto.CreateProductRequestDto;
import com.example.FakeCommerce.dto.GetProductResponseDto;
import com.example.FakeCommerce.dto.GetProductWithDetailsResponseDto;
import com.example.FakeCommerce.schema.Product;
import com.example.FakeCommerce.services.ProductService;
import com.example.FakeCommerce.utils.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<APIResponse<List<GetProductResponseDto>>> getAllProducts() {
        List<GetProductResponseDto> products = productService.getAllProducts();
        return ResponseEntity
                .ok(APIResponse.success(products, "Products fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<GetProductResponseDto>> getProductById(@PathVariable Long id) {
        GetProductResponseDto product = productService.getProductById(id);
        return ResponseEntity
                .ok(APIResponse.success(product, "Product fetched successfully"));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<APIResponse<GetProductWithDetailsResponseDto>> getProductWithDetailsById(@PathVariable Long id) {
        GetProductWithDetailsResponseDto product = productService.getProductDetialsById(id);
        return ResponseEntity
                .ok(APIResponse.success(product, "Product details fetched successfully"));
    }

    @PostMapping
    public ResponseEntity<APIResponse<Product>> createProduct(@RequestBody CreateProductRequestDto requestDto) {
        Product product = productService.createProduct(requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(APIResponse.success(product, "Product created successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity
                .ok(APIResponse.success(null, "Product deleted successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse<List<Product>>> getProductsByCategory(@RequestParam("categoryName") String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity
                .ok(APIResponse.success(products, "Products fetched successfully"));
    }

    @GetMapping("/categories")
    public ResponseEntity<APIResponse<List<String>>> getAllCategories() {
        List<String> categories = productService.getAllCatagories();
        return ResponseEntity
                .ok(APIResponse.success(categories, "Categories fetched successfully"));
    }
}

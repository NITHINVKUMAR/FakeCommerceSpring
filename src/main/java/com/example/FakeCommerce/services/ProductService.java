package com.example.FakeCommerce.services;

import com.example.FakeCommerce.dto.CreateProductRequestDto;
import com.example.FakeCommerce.repositories.ProductRepository;
import com.example.FakeCommerce.schema.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor  // Creates constructor for required fields (final and @NonNull)
public class ProductService {
    private final ProductRepository productRepository;
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    public Product saveProduct(CreateProductRequestDto createProduct){
        Product newProduct = Product.builder()
                .title(createProduct.getTitle())
                .description(createProduct.getDescription())
                .price(createProduct.getPrice())
                .image(createProduct.getImage())
                .category(createProduct.getCategory())
                .rating(createProduct.getRating())
                .build();
        return productRepository.save(newProduct);
    }
    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }
    public List<Product> getProductsByCategory(String category){
        return productRepository.getProductsByCategory(category);
    }
    public List<String> getAllCatagories(){
        return productRepository.getAllCategories();
    }
}

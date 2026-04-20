package com.example.FakeCommerce.services;

import com.example.FakeCommerce.dto.CreateProductRequestDto;
import com.example.FakeCommerce.dto.GetProductResponseDto;
import com.example.FakeCommerce.dto.GetProductWithDetailsResponseDto;
import com.example.FakeCommerce.exceptions.ResourceNotFoundException;
import com.example.FakeCommerce.repositories.ProductRepository;
import com.example.FakeCommerce.schema.Category;
import com.example.FakeCommerce.schema.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor  // Creates constructor for required fields (final and @NonNull)
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public List<GetProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
//        List<GetProductResponseDto> responseDtos = new ArrayList<>();
//        for (Product product : products) {
//            GetProductResponseDto responseDto = GetProductResponseDto.builder().
//                    id(product.getId())
//                    .title(product.getTitle())
//                    .description((product.getDescription()))
//                    .image((product.getImage()))
//                    .price((product.getPrice()))
//                    .rating((product.getRating()))
//                    .build();
//            responseDtos.add(responseDto);
//        }
//        return responseDtos;

        // Using Stream APIS
        return products.stream()
                .map(product -> GetProductResponseDto.builder()
                        .id(product.getId())
                        .title(product.getTitle())
                        .description((product.getDescription()))
                        .image((product.getImage()))
                        .price((product.getPrice()))
                        .rating((product.getRating()))
                        .build()
                )
                .collect(Collectors.toList());
    }
    public GetProductResponseDto getProductById(Long id){
        return productRepository.findById(id)
                .map(product -> GetProductResponseDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description((product.getDescription()))
                .image((product.getImage()))
                .price((product.getPrice()))
                .rating((product.getRating()))
                .build())
                .orElseThrow(()->new ResourceNotFoundException("Product with id " + id + " not found"));
    }
    public Product createProduct(CreateProductRequestDto requestDto){
        Category category = categoryService.getCategoryById(requestDto.getCategoryId());
        Product newProduct = Product.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .price(requestDto.getPrice())
                .image(requestDto.getImage())
                .category(category)
                .rating(requestDto.getRating())
                .build();
        return productRepository.save(newProduct);
    }
    public void deleteProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
        productRepository.delete(product);
    }
    public List<Product> getProductsByCategory(String category){
        return productRepository.getProductsByCategory(category);
    }
    public List<String> getAllCatagories(){
        return productRepository.getAllCategories();
    }

    public GetProductWithDetailsResponseDto getProductDetialsById(Long id){
        List<Product> results = productRepository.getProductDetailsById(id);
        if (results.isEmpty()) {
            throw new ResourceNotFoundException("Product with id " + id + " not found");
        }
        Product product = results.get(0);
         return GetProductWithDetailsResponseDto.builder()
                 .id(product.getId())
                 .title(product.getTitle())
                 .description(product.getDescription())
                 .image(product.getImage())
                 .rating(product.getRating())
                 .price(product.getPrice())
                 .category(product.getCategory().getName())
                 .build();
    }
}

package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.dto.CreateCategoryRequestDto;
import com.example.FakeCommerce.schema.Category;
import com.example.FakeCommerce.services.CategoryService;
import com.example.FakeCommerce.utils.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<APIResponse<Category>> createCategory(@RequestBody CreateCategoryRequestDto requestDto){
        Category category = categoryService.createCategory(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success(category,"Category Created Successfully"));
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<Category>>> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(categories,"Categories fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Category>> getCategoryById(@PathVariable  Long id){
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(category,"Categories fetched successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(null, "Category deleted successfully"));
    }
}

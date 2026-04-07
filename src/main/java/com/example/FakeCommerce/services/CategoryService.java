package com.example.FakeCommerce.services;

import com.example.FakeCommerce.dto.CreateCategoryRequestDto;
import com.example.FakeCommerce.repositories.CategoryRepository;
import com.example.FakeCommerce.schema.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category createCategory(CreateCategoryRequestDto requestDto){
        Category createNewCategory = Category.builder().name(requestDto.getName()).build();
        return categoryRepository.save(createNewCategory);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }
}

package com.example.FakeCommerce.services;

import com.example.FakeCommerce.dto.CreateCategoryRequestDto;
import com.example.FakeCommerce.exceptions.ResourceNotFoundException;
import com.example.FakeCommerce.repositories.CategoryRepository;
import com.example.FakeCommerce.schema.Category;
import com.example.FakeCommerce.services.cache.CategoryRedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryRedisCache categoryRedisCache;

    public Category createCategory(CreateCategoryRequestDto requestDto) {
        Category createNewCategory = Category.builder().name(requestDto.getName()).build();
        Category savedCategory = categoryRepository.save(createNewCategory);

        // Optionally cache the newly created category right away
        categoryRedisCache.putCategory(savedCategory.getId(), savedCategory);

        return savedCategory;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        // 1. Check cache first
        Optional<Category> cachedCategory = categoryRedisCache.getCategory(id);
        if (cachedCategory.isPresent()) {
            return cachedCategory.get();
        }

        // 2. Fallback to database
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with Id " + id + " Not Found"));

        // 3. Store in cache for subsequent calls
        categoryRedisCache.putCategory(id, category);

        return category;
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));

        categoryRepository.delete(category);

        // Evict from cache to prevent stale data hits
        categoryRedisCache.evictCategory(id);
    }
}
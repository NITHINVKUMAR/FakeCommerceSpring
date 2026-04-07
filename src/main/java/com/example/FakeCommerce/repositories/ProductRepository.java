package com.example.FakeCommerce.repositories;

import com.example.FakeCommerce.schema.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    public List<Product> getProductsByCategory(String category);

    @Query(nativeQuery = true,value = "SELECT DISTINCT category FROM products")
    public List<String> getAllCategories();
}

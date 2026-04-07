package com.example.FakeCommerce.repositories;

import com.example.FakeCommerce.schema.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> getProductsByCategory(String category);

    @Query(nativeQuery = true,value = "SELECT DISTINCT category FROM products")
    List<String> getAllCategories();

    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.id = :id")
    List<Product>getProductDetailsById(Long id);
}

package com.example.FakeCommerce.repositories;

import com.example.FakeCommerce.schema.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductsId(Long productId);

    List<Review> findByOrderId(Long orderId);
}

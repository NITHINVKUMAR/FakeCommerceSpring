package com.example.FakeCommerce.repositories;

import com.example.FakeCommerce.schema.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProducts,Long> {
    List<OrderProducts> findByOrderId(Long orderId);
}

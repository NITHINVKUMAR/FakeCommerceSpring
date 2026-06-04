package com.example.FakeCommerce.repositories;

import com.example.FakeCommerce.schema.Order;
import com.example.FakeCommerce.schema.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProducts,Long> {
    List<OrderProducts> findByOrderId(Long orderId);

    @Query("SELECT op FROM OrderProducts op JOIN FETCH op.product WHERE op.order = :order")
    List<OrderProducts> findByOrderWithProduct(Order order);
}

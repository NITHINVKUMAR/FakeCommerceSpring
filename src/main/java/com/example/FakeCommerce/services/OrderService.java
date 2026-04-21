package com.example.FakeCommerce.services;

import com.example.FakeCommerce.adapters.OrderAdapter;
import com.example.FakeCommerce.dto.GetOrderResponseDto;
import com.example.FakeCommerce.exceptions.ResourceNotFoundException;
import com.example.FakeCommerce.repositories.OrderProductRepository;
import com.example.FakeCommerce.repositories.OrderRepository;
import com.example.FakeCommerce.repositories.ProductRepository;
import com.example.FakeCommerce.schema.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final OrderAdapter orderAdapter;

    public List<GetOrderResponseDto> getAllOrders(){
        List<Order> orders = orderRepository.findAll();
        return orderAdapter.mapToGetOrderResponseDtoList(orders);
    }

    public GetOrderResponseDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return orderAdapter.mapToGetOrderResponseDto(order);
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        orderRepository.delete(order);
    }
}

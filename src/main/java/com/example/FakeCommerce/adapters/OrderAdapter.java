package com.example.FakeCommerce.adapters;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.example.FakeCommerce.dto.GetOrderResponseDto;
import com.example.FakeCommerce.dto.OrderItemResponseDto;
import org.springframework.stereotype.Component;

import com.example.FakeCommerce.repositories.OrderProductRepository;
import com.example.FakeCommerce.schema.Order;
import com.example.FakeCommerce.schema.OrderProducts;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class OrderAdapter {
    // TODO(https://github.com/singhsanket143/FakeCommerceSpring/issues/1): try to implement this using mapstruct
    private final OrderProductRepository orderproductsRepository;

    public List<GetOrderResponseDto> mapToGetOrderResponseDtoList(List<Order> orders) {
        return orders.stream()
                .map(this::mapToGetOrderResponseDto)
                .collect(Collectors.toList());
    }

    public GetOrderResponseDto mapToGetOrderResponseDto(Order order) {

        List<OrderProducts> orderProducts = orderproductsRepository.findByOrderId(order.getId());
        List<OrderItemResponseDto> items = mapToOrderItemResponseDto(orderProducts);

        return GetOrderResponseDto.builder()
                .id(order.getId())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(items)
                .build();

    }

    public List<OrderItemResponseDto> mapToOrderItemResponseDto(List<OrderProducts> orderProducts) {
        return orderProducts.stream()
                .map(op -> OrderItemResponseDto.builder()
                        .productId(op.getProducts().getId())
                        .quantity(op.getQuantity())
                        .productName(op.getProducts().getTitle())
                        .productPrice(op.getProducts().getPrice())
                        .productImage(op.getProducts().getImage())
                        .subTotal(op.getProducts().getPrice().multiply(BigDecimal.valueOf(op.getQuantity())))
                        .build())
                .collect(Collectors.toList());
    }
}
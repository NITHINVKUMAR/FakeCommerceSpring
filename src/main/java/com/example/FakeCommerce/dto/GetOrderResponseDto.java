package com.example.FakeCommerce.dto;

import com.example.FakeCommerce.schema.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetOrderResponseDto {
    private Long id;

    private OrderStatus status;

    private List<OrderItemResponseDto> items;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

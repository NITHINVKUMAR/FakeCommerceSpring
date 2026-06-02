package com.example.FakeCommerce.dto;

import com.example.FakeCommerce.schema.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderRequestDto {
    private OrderStatus status;

    private List<OrderItemActionDto> orderItems;
}

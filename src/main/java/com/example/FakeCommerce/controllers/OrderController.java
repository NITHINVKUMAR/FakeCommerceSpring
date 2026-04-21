package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.dto.GetOrderResponseDto;
import com.example.FakeCommerce.services.OrderService;
import com.example.FakeCommerce.utils.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<APIResponse<List<GetOrderResponseDto>>>getAllOrders(){
        List<GetOrderResponseDto> orders = orderService.getAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(orders,"Fetched all orders successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<GetOrderResponseDto>>getOrderById(@PathVariable Long id){
        GetOrderResponseDto order = orderService.getOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(order,"Fetched order successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(null,"Order Deleted Successfully"));
    }
}

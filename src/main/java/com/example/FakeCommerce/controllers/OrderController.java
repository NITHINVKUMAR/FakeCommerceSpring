package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.dto.CreateOrderRequestDto;
import com.example.FakeCommerce.dto.GetOrderResponseDto;
import com.example.FakeCommerce.dto.GetOrderSummaryResponseDto;
import com.example.FakeCommerce.dto.UpdateOrderRequestDto;
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

    @PostMapping
    public ResponseEntity<APIResponse<GetOrderResponseDto>> createOrder(@RequestBody CreateOrderRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success(orderService.createOrder(requestDto),"Order Created Successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<GetOrderResponseDto>> updateOrder(@PathVariable Long id, @RequestBody UpdateOrderRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success(orderService.updateOrder(id,requestDto),"Order Updated successfully"));
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<APIResponse<GetOrderSummaryResponseDto>> getOrderSummary(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIResponse.success(orderService.getOrderSummary(id), "Order summary fetched successfully"));
    }
}

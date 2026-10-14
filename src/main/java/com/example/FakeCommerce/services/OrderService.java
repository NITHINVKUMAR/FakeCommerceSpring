package com.example.FakeCommerce.services;

import com.example.FakeCommerce.adapters.OrderAdapter;
import com.example.FakeCommerce.dto.*;
import com.example.FakeCommerce.exceptions.ResourceNotFoundException;
import com.example.FakeCommerce.repositories.OrderProductRepository;
import com.example.FakeCommerce.repositories.OrderRepository;
import com.example.FakeCommerce.repositories.ProductRepository;
import com.example.FakeCommerce.schema.Order;
import com.example.FakeCommerce.schema.OrderProducts;
import com.example.FakeCommerce.schema.OrderStatus;
import com.example.FakeCommerce.schema.Product;
import com.example.FakeCommerce.services.cache.OrderRedisCache;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final OrderAdapter orderAdapter;
    private final OrderRedisCache orderRedisCache;

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

        // Evict summary from cache
        orderRedisCache.evictOrderSummary(id);
    }

    @Transactional
    public GetOrderResponseDto createOrder(CreateOrderRequestDto createOrderRequestDto) {
        Order order = Order.builder().status(OrderStatus.PENDING).build();
        orderRepository.save(order);

        if (createOrderRequestDto.getOrderItems() != null) {
            List<Long> productIds = createOrderRequestDto.getOrderItems().stream()
                    .map(item -> item.getProductId()).collect(Collectors.toList());
            List<Product> products = productRepository.findAllById(productIds);
            Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
            for (Long id : productIds) {
                if (!productMap.containsKey(id)) {
                    throw new ResourceNotFoundException("Product not found with id: " + id);
                }
            }
            List<OrderProducts> orderProducts = new ArrayList<>();
            for (var itemDto : createOrderRequestDto.getOrderItems()) {
                Product product = productMap.get(itemDto.getProductId());

                orderProducts.add(OrderProducts.builder()
                        .order(order)
                        .products(product)
                        .quantity(itemDto.getQuantity() != null ? itemDto.getQuantity() : 1)
                        .build());
            }

            orderProductRepository.saveAll(orderProducts);
        }

        // Pre-cache order summary upon creation if desired, or let it load lazily
        return orderAdapter.mapToGetOrderResponseDto(order);
    }

    @Transactional
    public GetOrderResponseDto updateOrder(Long id, UpdateOrderRequestDto updateOrderRequestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if(updateOrderRequestDto.getStatus() != null) {
            order.setStatus(updateOrderRequestDto.getStatus());
            orderRepository.save(order);
        }

        if(updateOrderRequestDto.getOrderItems() != null) {
            List<Long> productIds = updateOrderRequestDto.getOrderItems().stream().map(item -> item.getProductId()).collect(Collectors.toList());
            List<Product> products = productRepository.findAllById(productIds);
            Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));

            for(var pid : productIds) {
                if(!productMap.containsKey(pid)) {
                    throw new ResourceNotFoundException("Product not found with id: " + pid);
                }
            }

            List<OrderProducts> toSave = new ArrayList<>();
            List<OrderProducts> toDelete = new ArrayList<>();

            Map<Long, OrderProducts> existingItems = orderProductRepository.findByOrderWithProduct(order)
                    .stream().collect(Collectors.toMap(op -> op.getProducts().getId(), Function.identity()));

            for(OrderItemActionDto itemAction : updateOrderRequestDto.getOrderItems()) {
                Product product = productMap.get(itemAction.getProductId());
                OrderProducts existing = existingItems.get(product.getId());

                switch(itemAction.getAction()) {
                    case ADD -> {
                        if(existing != null) {
                            int addQty = (itemAction.getQuantity() != null ? itemAction.getQuantity() : 1);
                            existing.setQuantity(existing.getQuantity() + addQty);
                            toSave.add(existing);
                        } else {
                            OrderProducts newItem = OrderProducts
                                    .builder()
                                    .order(order)
                                    .products(product)
                                    .quantity(itemAction.getQuantity() != null ? itemAction.getQuantity() : 1)
                                    .build();
                            existingItems.put(product.getId(), newItem);
                            toSave.add(newItem);
                        }
                    }
                    case REMOVE -> {
                        if(existing == null) {
                            throw new ResourceNotFoundException("Product not found with id: " + product.getId());
                        }
                        toDelete.add(existing);
                        existingItems.remove(product.getId());
                    }
                    case INCREMENT -> {
                        if(existing == null) {
                            throw new ResourceNotFoundException("Product not found with id: " + product.getId());
                        }
                        existing.setQuantity(existing.getQuantity() + 1);
                        toSave.add(existing);
                    }
                    case DECREMENT -> {
                        if(existing == null) {
                            throw new ResourceNotFoundException("Product not found with id: " + product.getId());
                        }
                        if(existing.getQuantity() <= 1) {
                            toDelete.add(existing);
                            existingItems.remove(product.getId());
                        } else {
                            existing.setQuantity(existing.getQuantity() - 1);
                            toSave.add(existing);
                        }
                    }
                }
            }

            if(!toSave.isEmpty()) {
                orderProductRepository.saveAll(toSave);
            }
            if(!toDelete.isEmpty()) {
                orderProductRepository.deleteAll(toDelete);
            }
        }

        // Evict or update cache since order items/status changed
        orderRedisCache.evictOrderSummary(id);

        return orderAdapter.mapToGetOrderResponseDto(order);
    }

    public GetOrderSummaryResponseDto getOrderSummary(Long id) {
        // 1. Check cache first
        Optional<GetOrderSummaryResponseDto> cachedSummary = orderRedisCache.getOrderSummary(id);
        if (cachedSummary.isPresent()) {
            return cachedSummary.get();
        }

        // 2. Fetch from DB
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        List<OrderProducts> orderProducts = orderProductRepository.findByOrderWithProduct(order);
        List<OrderItemResponseDto> items = orderAdapter.mapToOrderItemResponseDto(orderProducts);

        int totalItems = orderProducts.stream().mapToInt(OrderProducts::getQuantity).sum();

        BigDecimal totalPrice = orderProducts.stream().map(op -> op.getProducts().getPrice().multiply(BigDecimal.valueOf(op.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        GetOrderSummaryResponseDto summary = GetOrderSummaryResponseDto.builder()
                .id(order.getId())
                .status(order.getStatus())
                .items(items)
                .totalItems(totalItems)
                .totalPrice(totalPrice)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();

        // 3. Put into cache
        orderRedisCache.putOrderSummary(id, summary);

        return summary;
    }
}
package com.proiect.restaurant.repository;

import com.proiect.restaurant.entity.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository {
    OrderItem save(OrderItem orderItem);
    Optional<OrderItem> findById(Long id);
    List<OrderItem> findAll();
    void deleteById(Long id);
    List<OrderItem> findByOrderId(Long orderId);
}

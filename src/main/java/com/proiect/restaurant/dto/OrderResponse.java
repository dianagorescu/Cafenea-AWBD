package com.proiect.restaurant.dto;

import com.proiect.restaurant.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    
    private Long id;
    private LocalDateTime orderTime;
    private OrderStatus status;
    private double totalPrice;
    private Long customerId;
    private String customerName;
    private List<OrderItemResponse> items;
    
    public OrderResponse() {}
    
    public OrderResponse(Long id, LocalDateTime orderTime, OrderStatus status, double totalPrice,
                        Long customerId, String customerName, List<OrderItemResponse> items) {
        this.id = id;
        this.orderTime = orderTime;
        this.status = status;
        this.totalPrice = totalPrice;
        this.customerId = customerId;
        this.customerName = customerName;
        this.items = items;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getOrderTime() {
        return orderTime;
    }
    
    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public Long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public List<OrderItemResponse> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }
}

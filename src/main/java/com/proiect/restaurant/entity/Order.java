package com.proiect.restaurant.entity;

import java.time.LocalDateTime;

public class Order {

    private Long id;

    private LocalDateTime orderTime;

    private OrderStatus status = OrderStatus.CREATED;

    private double totalPrice = 0.0;

    private Long customerId;

    public Order() {
        this.orderTime = LocalDateTime.now();
    }

    public Order(Long customerId) {
        this();
        this.customerId = customerId;
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
}

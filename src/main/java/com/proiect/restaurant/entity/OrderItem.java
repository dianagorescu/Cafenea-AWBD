package com.proiect.restaurant.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderItem {

    private Long id;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private double price;

    private Long orderId;
    private Long menuItemId;

    public OrderItem() {}

    public OrderItem(Integer quantity, double price, Long orderId, Long menuItemId) {
        this.quantity = quantity;
        this.price = price;
        this.orderId = orderId;
        this.menuItemId = menuItemId;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }
}

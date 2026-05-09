package com.proiect.restaurant.dto;

import com.proiect.restaurant.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public class OrderStatusUpdateRequest {
    
    @NotNull(message = "Status is required")
    private OrderStatus status;
    
    public OrderStatusUpdateRequest() {}
    
    public OrderStatusUpdateRequest(OrderStatus status) {
        this.status = status;
    }
    
    // Getters and Setters
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}

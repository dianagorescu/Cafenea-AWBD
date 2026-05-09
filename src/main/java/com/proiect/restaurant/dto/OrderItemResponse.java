package com.proiect.restaurant.dto;

public class OrderItemResponse {
    
    private Long id;
    private String menuItemName;
    private Integer quantity;
    private double price;
    
    public OrderItemResponse() {}
    
    public OrderItemResponse(Long id, String menuItemName, Integer quantity, double price) {
        this.id = id;
        this.menuItemName = menuItemName;
        this.quantity = quantity;
        this.price = price;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getMenuItemName() {
        return menuItemName;
    }
    
    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
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
}

package com.proiect.restaurant.dto;

public class CafeTableResponse {
    
    private Long id;
    private Integer tableNumber;
    private Integer capacity;
    private Boolean available;
    
    public CafeTableResponse() {}
    
    public CafeTableResponse(Long id, Integer tableNumber, Integer capacity, Boolean available) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.available = available;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getTableNumber() {
        return tableNumber;
    }
    
    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    
    public Boolean getAvailable() {
        return available;
    }
    
    public void setAvailable(Boolean available) {
        this.available = available;
    }
}

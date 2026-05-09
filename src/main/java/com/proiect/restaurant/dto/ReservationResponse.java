package com.proiect.restaurant.dto;

import com.proiect.restaurant.entity.ReservationStatus;

import java.time.LocalDateTime;

public class ReservationResponse {
    
    private Long id;
    private LocalDateTime reservationTime;
    private Integer duration;
    private ReservationStatus status;
    private Long customerId;
    private String customerName;
    private Long tableId;
    private Integer tableNumber;
    
    public ReservationResponse() {}
    
    public ReservationResponse(Long id, LocalDateTime reservationTime, Integer duration, 
                              ReservationStatus status, Long customerId, String customerName,
                              Long tableId, Integer tableNumber) {
        this.id = id;
        this.reservationTime = reservationTime;
        this.duration = duration;
        this.status = status;
        this.customerId = customerId;
        this.customerName = customerName;
        this.tableId = tableId;
        this.tableNumber = tableNumber;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getReservationTime() {
        return reservationTime;
    }
    
    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    public ReservationStatus getStatus() {
        return status;
    }
    
    public void setStatus(ReservationStatus status) {
        this.status = status;
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
    
    public Long getTableId() {
        return tableId;
    }
    
    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }
    
    public Integer getTableNumber() {
        return tableNumber;
    }
    
    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }
}

package com.proiect.restaurant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "receipts")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Receipt number is required")
    @Column(name = "receipt_number", nullable = false, unique = true)
    private String receiptNumber;

    @NotNull(message = "Issue time is required")
    @Column(name = "issue_time", nullable = false)
    private LocalDateTime issueTime;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    public Receipt() {
        this.issueTime = LocalDateTime.now();
    }

    public Receipt(String receiptNumber, double totalPrice, Order order) {
        this();
        this.receiptNumber = receiptNumber;
        this.totalPrice = totalPrice;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public LocalDateTime getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(LocalDateTime issueTime) {
        this.issueTime = issueTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}

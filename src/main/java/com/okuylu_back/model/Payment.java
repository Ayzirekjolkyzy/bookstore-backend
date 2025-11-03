package com.okuylu_back.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payment_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User paymentHolder;

    private BigDecimal amount;

    @Column(name = "payment_intent_id", unique = true)
    private String paymentIntentId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "is_paid")
    private boolean isPaid = false;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "last_error_message")
    private String lastErrorMessage;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(Long payment_id) {
        this.payment_id = payment_id;
    }

    public User getPaymentHolder() {
        return paymentHolder;
    }

    public void setPaymentHolder(User paymentHolder) {
        this.paymentHolder = paymentHolder;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentIntentId() {
        return paymentIntentId;
    }

    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public void setLastErrorMessage(String lastErrorMessage) {
        this.lastErrorMessage = lastErrorMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Payment() {
    }

    public Payment(Long payment_id, User paymentHolder, BigDecimal amount, String paymentIntentId, Long orderId, boolean isPaid, LocalDateTime paymentDate, String lastErrorMessage, String status, LocalDateTime createdAt) {
        this.payment_id = payment_id;
        this.paymentHolder = paymentHolder;
        this.amount = amount;
        this.paymentIntentId = paymentIntentId;
        this.orderId = orderId;
        this.isPaid = isPaid;
        this.paymentDate = paymentDate;
        this.lastErrorMessage = lastErrorMessage;
        this.status = status;
        this.createdAt = createdAt;
    }
}
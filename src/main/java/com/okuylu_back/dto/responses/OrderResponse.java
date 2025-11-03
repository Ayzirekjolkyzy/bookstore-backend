package com.okuylu_back.dto.responses;

import com.okuylu_back.model.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long orderId;
    private String userEmail;
    private OrderStatus status;
    private boolean selfPickup;
    private BigDecimal totalAmount;
    private BigDecimal discountedPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String deliveryAddress;
    private String phoneNumber;
    private String additionalNotes;
    private String managerEmail;
    private String managerPhone;
    private List<OrderItemResponse> orderItems;
    private BigDecimal deliveryCost;
    private boolean isReceived;



    public OrderResponse() {}

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public OrderResponse(Long orderId, String userEmail, OrderStatus status, boolean selfPickup, BigDecimal totalAmount, BigDecimal discountedPrice, LocalDateTime createdAt, LocalDateTime updatedAt, String deliveryAddress, String phoneNumber, String additionalNotes, String managerEmail, String managerPhone, List<OrderItemResponse> orderItems, BigDecimal deliveryCost, boolean isReceived) {
        this.orderId = orderId;
        this.userEmail = userEmail;
        this.status = status;
        this.selfPickup = selfPickup;
        this.totalAmount = totalAmount;
        this.discountedPrice = discountedPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deliveryAddress = deliveryAddress;
        this.phoneNumber = phoneNumber;
        this.additionalNotes = additionalNotes;
        this.managerEmail = managerEmail;
        this.managerPhone = managerPhone;
        this.orderItems = orderItems;
        this.deliveryCost = deliveryCost;
        this.isReceived = isReceived;
    }



    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }


    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }

    public boolean isSelfPickup() {
        return selfPickup;
    }

    public void setSelfPickup(boolean selfPickup) {
        this.selfPickup = selfPickup;
    }

    public BigDecimal getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(BigDecimal deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public boolean isReceived() {
        return isReceived;
    }

    public void setReceived(boolean received) {
        isReceived = received;
    }
}

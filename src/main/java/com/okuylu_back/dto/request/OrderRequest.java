package com.okuylu_back.dto.request;

import jakarta.validation.constraints.*;

import java.util.List;

public class OrderRequest {
    @NotNull(message = "Delivery method is required")
    private boolean selfPickup;

    @Size(min = 2, max = 100, message = "Phone number must be between 2 and 100 characters")
    private String phoneNumber;

    private String deliveryAddress; // Required only for DELIVERY method

    private String additionalNotes;

    @NotNull(message = "Order items are required")
    private List<OrderItemRequest> orderItems;

    public OrderRequest() {
    }

    @NotNull(message = "Delivery method is required")
    public boolean isSelfPickup() {
        return selfPickup;
    }

    public void setSelfPickup(@NotNull(message = "Delivery method is required") boolean selfPickup) {
        this.selfPickup = selfPickup;
    }

    public @Size(min = 2, max = 100, message = "Phone number must be between 2 and 100 characters") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@Size(min = 2, max = 100, message = "Phone number must be between 2 and 100 characters") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemRequest> orderItems) {
        this.orderItems = orderItems;
    }

    public OrderRequest(boolean selfPickup, String phoneNumber, String deliveryAddress, String additionalNotes, List<OrderItemRequest> orderItems) {
        this.selfPickup = selfPickup;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
        this.additionalNotes = additionalNotes;
        this.orderItems = orderItems;
    }
}

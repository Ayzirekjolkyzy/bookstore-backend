package com.okuylu_back.dto.responses;
public class OrderStatusResponse {
    private Long orderId;
    private String status;
    private boolean isPaid;

    public OrderStatusResponse() {
    }

    public OrderStatusResponse(Long orderId, String status, boolean isPaid) {
        this.orderId = orderId;
        this.status = status;
        this.isPaid = isPaid;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}

package com.okuylu_back.dto.responses;

public class PaymentResponse {
    private String sessionId;
    private Long orderId;
    private String currency;
    private double amount;
    private String checkoutUrl;

    public PaymentResponse() {
    }

    public PaymentResponse(String sessionId, Long orderId, String currency, double amount, String checkoutUrl) {
        this.sessionId = sessionId;
        this.orderId = orderId;
        this.currency = currency;
        this.amount = amount;
        this.checkoutUrl = checkoutUrl;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }
}

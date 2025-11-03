package com.okuylu_back.dto.responses;

public class ManagerPerformanceDto {

    private String email;
    private String phone;
    private long completedOrders;

    public ManagerPerformanceDto(String email, String phone, long completedOrders) {
        this.email = email;
        this.phone = phone;
        this.completedOrders = completedOrders;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(long completedOrders) {
        this.completedOrders = completedOrders;
    }
}

package com.okuylu_back.dto.responses;

public class ManagerStatisticsDto {
    private int totalOrders;
    private int deliveredOrders;
    private int shippedOrders;
    private int processingOrders;
    private int cancelledOrders;
    private int pickedUpOrders;
    private double averageProcessingTimeInHours;
    private int deliveredAndPickedUp;


    public ManagerStatisticsDto(int totalOrders, int deliveredOrders, int shippedOrders, int processingOrders, int cancelledOrders, int pickedUpOrders, double averageProcessingTimeInHours, int deliveredAndPickedUp) {
        this.totalOrders = totalOrders;
        this.deliveredOrders = deliveredOrders;
        this.shippedOrders = shippedOrders;
        this.processingOrders = processingOrders;
        this.cancelledOrders = cancelledOrders;
        this.pickedUpOrders = pickedUpOrders;
        this.averageProcessingTimeInHours = averageProcessingTimeInHours;
        this.deliveredAndPickedUp = deliveredAndPickedUp;
    }

    public ManagerStatisticsDto() {
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getDeliveredOrders() {
        return deliveredOrders;
    }

    public void setDeliveredOrders(int deliveredOrders) {
        this.deliveredOrders = deliveredOrders;
    }

    public int getShippedOrders() {
        return shippedOrders;
    }

    public void setShippedOrders(int shippedOrders) {
        this.shippedOrders = shippedOrders;
    }

    public int getProcessingOrders() {
        return processingOrders;
    }

    public void setProcessingOrders(int processingOrders) {
        this.processingOrders = processingOrders;
    }

    public int getCancelledOrders() {
        return cancelledOrders;
    }

    public void setCancelledOrders(int cancelledOrders) {
        this.cancelledOrders = cancelledOrders;
    }

    public int getPickedUpOrders() {
        return pickedUpOrders;
    }

    public void setPickedUpOrders(int pickedUpOrders) {
        this.pickedUpOrders = pickedUpOrders;
    }

    public double getAverageProcessingTimeInHours() {
        return averageProcessingTimeInHours;
    }

    public void setAverageProcessingTimeInHours(double averageProcessingTimeInHours) {
        this.averageProcessingTimeInHours = averageProcessingTimeInHours;
    }

    public int getDeliveredAndPickedUp() {
        return deliveredAndPickedUp;
    }

    public void setDeliveredAndPickedUp(int deliveredAndPickedUp) {
        this.deliveredAndPickedUp = deliveredAndPickedUp;
    }


}

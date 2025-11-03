package com.okuylu_back.dto.responses;

import com.okuylu_back.model.Book;
import com.okuylu_back.model.enums.OrderStatus;

import java.util.List;
import java.util.Map;

public class AdminStatisticsDto {
    private int totalOrders;
    private double totalRevenue;
    private Map<OrderStatus, Long> ordersByStatus;
    private Long totalBooks;
    private Long outOfStockBooks;
    private Long totalStockQuantity;
    private Long totalUsers;
    private Long verifiedUsers;
    private Long newUsers;
    private Long totalManagers;
    private List<ManagerPerformanceDto> topManagers;
    private double averageProcessingTimeHours;

    public AdminStatisticsDto(int totalOrders, double totalRevenue,
                              Map<OrderStatus, Long> ordersByStatus, Long totalBooks,
                              Long outOfStockBooks, Long totalStockQuantity,
                              Long totalUsers, Long verifiedUsers, Long newUsers,
                              Long totalManagers, List<ManagerPerformanceDto> topManagers,
                              double averageProcessingTimeHours) {
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
        this.ordersByStatus = ordersByStatus;
        this.totalBooks = totalBooks;
        this.outOfStockBooks = outOfStockBooks;
        this.totalStockQuantity = totalStockQuantity;
        this.totalUsers = totalUsers;
        this.verifiedUsers = verifiedUsers;
        this.newUsers = newUsers;
        this.totalManagers = totalManagers;
        this.topManagers = topManagers;
        this.averageProcessingTimeHours = averageProcessingTimeHours;
    }

    @Override
    public String toString() {
        return "AdminStatisticsDto{" +
                "totalOrders=" + totalOrders +
                ", totalRevenue=" + totalRevenue +
                ", ordersByStatus=" + ordersByStatus +
                ", totalBooks=" + totalBooks +
                ", outOfStockBooks=" + outOfStockBooks +
                ", totalStockQuantity=" + totalStockQuantity +
                ", totalUsers=" + totalUsers +
                ", verifiedUsers=" + verifiedUsers +
                ", newUsersLastMonth=" + newUsers +
                ", totalManagers=" + totalManagers +
                ", topManagers=" + topManagers +
                ", averageProcessingTimeHours=" + averageProcessingTimeHours +
                '}';
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }


    public Map<OrderStatus, Long> getOrdersByStatus() {
        return ordersByStatus;
    }

    public void setOrdersByStatus(Map<OrderStatus, Long> ordersByStatus) {
        this.ordersByStatus = ordersByStatus;
    }

    public Long getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(Long totalBooks) {
        this.totalBooks = totalBooks;
    }

    public Long getOutOfStockBooks() {
        return outOfStockBooks;
    }

    public void setOutOfStockBooks(Long outOfStockBooks) {
        this.outOfStockBooks = outOfStockBooks;
    }

    public Long getTotalStockQuantity() {
        return totalStockQuantity;
    }

    public void setTotalStockQuantity(Long totalStockQuantity) {
        this.totalStockQuantity = totalStockQuantity;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getVerifiedUsers() {
        return verifiedUsers;
    }

    public void setVerifiedUsers(Long verifiedUsers) {
        this.verifiedUsers = verifiedUsers;
    }

    public Long getNewUsers() {
        return newUsers;
    }

    public void setNewUsers(Long newUsers) {
        this.newUsers = newUsers;
    }

    public Long getTotalManagers() {
        return totalManagers;
    }

    public void setTotalManagers(Long totalManagers) {
        this.totalManagers = totalManagers;
    }

    public List<ManagerPerformanceDto> getTopManagers() {
        return topManagers;
    }

    public void setTopManagers(List<ManagerPerformanceDto> topManagers) {
        this.topManagers = topManagers;
    }

    public double getAverageProcessingTimeHours() {
        return averageProcessingTimeHours;
    }

    public void setAverageProcessingTimeHours(double averageProcessingTimeHours) {
        this.averageProcessingTimeHours = averageProcessingTimeHours;
    }
}

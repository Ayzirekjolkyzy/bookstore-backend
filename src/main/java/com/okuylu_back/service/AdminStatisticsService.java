package com.okuylu_back.service;


import com.okuylu_back.dto.responses.AdminStatisticsDto;
import com.okuylu_back.dto.responses.ManagerPerformanceDto;
import com.okuylu_back.model.Book;
import com.okuylu_back.model.Order;
import com.okuylu_back.model.enums.OrderStatus;
import com.okuylu_back.repository.BookRepository;
import com.okuylu_back.repository.OrderRepository;
import com.okuylu_back.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class AdminStatisticsService {

        public AdminStatisticsService(OrderRepository orderRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    public AdminStatisticsDto getAdminStatistics() {
        return getAdminStatisticsByDate(null, null);
    }

    @Transactional
    public AdminStatisticsDto getAdminStatisticsByDate(LocalDateTime startDate, LocalDateTime endDate) {

        List<Order> orders = (startDate != null && endDate != null)
                ? orderRepository.findByCreatedAtBetween(startDate, endDate)
                : orderRepository.findAll();

        int totalOrders = orders.size();
        double totalRevenue = calculateTotalRevenue(orders);
        Map<OrderStatus, Long> ordersByStatus = groupOrdersByStatus(orders);


        Long totalBooks = bookRepository.count();
        Long outOfStockBooks = bookRepository.countByStockQuantity(0);
        Long totalStockQuantity = bookRepository.getTotalStockQuantity(); // всего экземпляров



        Long totalUsers = userRepository.count();
        Long newUsers = (startDate != null && endDate != null)
                ? userRepository.countByCreatedAtBetween(startDate, endDate)
                : userRepository.countByCreatedAtAfter(LocalDateTime.now().minusMonths(1));
        Long verifiedUsers = userRepository.countByIsEmailVerifiedTrue();

        Long totalManagers = userRepository.countByRoleName("MANAGER");



        List<ManagerPerformanceDto> topManagers1 = (startDate != null && endDate != null)
                ? orderRepository.findTopManagersByCompletedOrders(
                    startDate, endDate, PageRequest.of(0, 5))
                : orderRepository.findTopManagersByCompletedOrders(
                        LocalDateTime.now().minusMonths(1), LocalDateTime.now(), PageRequest.of(0, 5));

        Double avgMinutes = (startDate != null && endDate != null)
                ?  orderRepository.findAverageProcessingTimeInMinutes(startDate, endDate)
                : orderRepository.findAverageProcessingTimeInMinutes(LocalDateTime.now().minusMonths(1), LocalDateTime.now());
        double averageProcessingTimeHours = 0;
        if (avgMinutes != null) {
            averageProcessingTimeHours = Math.max(0, avgMinutes / 60.0);  // Не меньше 0
        }


        return new AdminStatisticsDto(
                totalOrders,
                totalRevenue,
                ordersByStatus,
                totalBooks,
                outOfStockBooks,
                totalStockQuantity,
                totalUsers,
                verifiedUsers,
                newUsers,
                totalManagers,
                topManagers1,
                averageProcessingTimeHours
        );

    }

    private double calculateTotalRevenue(List<Order> orders) {
        return orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED ||
                        o.getStatus() == OrderStatus.PICKED_UP)
                .mapToDouble(o -> o.getDiscountedPrice() != null ? o.getDiscountedPrice().doubleValue() : 0.0)
                .sum();
    }


    private Map<OrderStatus, Long> groupOrdersByStatus(List<Order> orders) {
        Map<OrderStatus, Long> statusCounts = Arrays.stream(OrderStatus.values())
                .collect(Collectors.toMap(
                        status -> status,
                        status -> 0L,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));

        orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getStatus,
                        Collectors.counting()
                ))
                .forEach(statusCounts::put);

        return statusCounts;
    }
}
package com.okuylu_back.service;

import com.okuylu_back.dto.request.OrderRequest;
import com.okuylu_back.dto.responses.*;
import com.okuylu_back.model.Order;
import com.okuylu_back.model.OrderItem;
import com.okuylu_back.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface OrderService {
    PaymentResponse createOrder(String userEmail, OrderRequest orderRequest);
    OrderResponse confirmPaymentBySessionId(String sessionId);
    OrderResponse getOrderByIdAndEmail(Long orderId, String email);
    OrderResponse confirmOrderDelivery(Long orderId, String userEmail);
    void processSuccessfulPayment(Long orderId);
    PageResponse<OrderResponse> getUserOrders(String userEmail, int page, int size);

    OrderResponse assignOrderToManager(Long orderId, String managerEmail);
    PageResponse<OrderResponse> getUnassignedOrders(int page, int size);
    PageResponse<OrderResponse> getManagerOrders(String managerEmail, int page, int size);
    Order getOrderByIdForManager(Long orderId);
    OrderResponse setDeliveryCost(Long orderId, BigDecimal deliveryCost, String managerEmail);
    PageResponse<OrderResponse> getOrdersByStatusForManager(OrderStatus status, String managerEmail, int page, int size);
    OrderResponse updateOrderStatusByManager(Long orderId, OrderStatus newStatus, String managerEmail);

    OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus);
    PageResponse<OrderResponse> getAllOrdersForAdmin(int page, int size);
    PageResponse<OrderResponse> getOrdersByStatusForAdmin(OrderStatus status, int page, int size);
    ManagerStatisticsDto getManagerStatisticsByDate(String managerEmail,LocalDate startDate, LocalDate endDate);

    OrderItemResponse mapToOrderItemResponse(OrderItem orderItem);
    OrderResponse mapToOrderResponse(Order order);







    }

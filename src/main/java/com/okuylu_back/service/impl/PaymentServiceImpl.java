package com.okuylu_back.service.impl;


import com.okuylu_back.dto.responses.OrderItemResponse;
import com.okuylu_back.dto.responses.OrderResponse;
import com.okuylu_back.model.Order;
import com.okuylu_back.repository.OrderRepository;
import com.okuylu_back.service.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl  {

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public PaymentServiceImpl(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    public OrderResponse confirmOrderBySessionId(String sessionId) throws StripeException {
        Session session = Session.retrieve(sessionId);

        if (!"complete".equalsIgnoreCase(session.getStatus())) {
            throw new IllegalStateException("Оплата не завершена. Статус: " + session.getStatus());
        }

        String orderIdStr = session.getMetadata().get("orderId");
        if (orderIdStr == null) {
            throw new IllegalArgumentException("Order ID не найден в metadata Stripe-сессии");
        }

        Long orderId = Long.parseLong(orderIdStr);

        orderService.processSuccessfulPayment(orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getOrderItemId(),
                        item.getOrder().getOrderId(),
                        item.getBook().getBookId(),
                        item.getBook().getTitle(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .toList();

        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setUserEmail(order.getUser().getEmail());
        response.setStatus(order.getStatus());
        response.setSelfPickup(order.isSelfPickup());
        response.setTotalAmount(order.getTotalAmount());
        response.setDiscountedPrice(order.getDiscountedPrice());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        response.setDeliveryAddress(order.getDeliveryAddress());
        response.setPhoneNumber(order.getPhoneNumber());
        response.setAdditionalNotes(order.getAdditionalNotes());
        response.setOrderItems(itemResponses);
        response.setDeliveryCost(order.getDeliveryCost());
        response.setReceived(order.isReceived());

        if (order.getAssignedManager() != null) {
            response.setManagerEmail(order.getAssignedManager().getEmail());
            response.setManagerPhone(order.getAssignedManager().getPhone());
        }

        return response;
    }

}

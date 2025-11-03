package com.okuylu_back.controller.user;

import com.okuylu_back.dto.request.OrderRequest;
import com.okuylu_back.dto.responses.OrderResponse;
import com.okuylu_back.dto.responses.PageResponse;
import com.okuylu_back.dto.responses.PaymentResponse;
import com.okuylu_back.service.OrderService;
import com.okuylu_back.service.impl.PaymentServiceImpl;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Order Controller")
public class OrderController {

    private final OrderService orderService;
    private final PaymentServiceImpl paymentService;

    public OrderController(OrderService orderService, PaymentServiceImpl paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @Operation(summary = "Создать новый заказ")
    @PostMapping("/orders/create")
    public ResponseEntity<PaymentResponse> createOrder(
            Authentication authentication,
            @Valid @RequestBody OrderRequest orderRequest
    ) {
        String email = authentication.getName();
        PaymentResponse order = orderService.createOrder(email, orderRequest);
        return ResponseEntity.ok(order);
    }


    @Operation(summary = "Получить список заказов пользователя с пагинацией")
    @GetMapping("/orders/my-orders")
    public PageResponse<OrderResponse> getUserOrders(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size

    ) {
        String email = authentication.getName();
        return orderService.getUserOrders(email, page, size);
    }

    @Operation(summary = "Получить детали заказа по ID")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long orderId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        OrderResponse order = orderService.getOrderByIdAndEmail(orderId, email);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Подтвердить получение заказа пользователем")
    @PutMapping("/orders/{orderId}/confirm-delivery")
    public ResponseEntity<OrderResponse> confirmOrderDelivery(
            @PathVariable Long orderId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        OrderResponse order = orderService.confirmOrderDelivery(orderId, email);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/payments/confirm")
    public ResponseEntity<OrderResponse> confirmPayment(@RequestParam String sessionId) throws StripeException {
        OrderResponse order = paymentService.confirmOrderBySessionId(sessionId);
        return ResponseEntity.ok(order);
    }


}
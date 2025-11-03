package com.okuylu_back.controller.user;

import com.okuylu_back.dto.request.OrderRequest;
import com.okuylu_back.dto.responses.PaymentResponse;
import com.okuylu_back.repository.OrderRepository;
import com.okuylu_back.service.OrderService;
import com.okuylu_back.service.StripeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/payments")
public class PaymentController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final StripeService stripeService;
    public PaymentController(OrderService orderService, OrderRepository orderRepository, StripeService stripeService) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.stripeService = stripeService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<PaymentResponse> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody OrderRequest orderRequest) {

        PaymentResponse response = orderService.createOrder(userDetails.getUsername(), orderRequest);
        return ResponseEntity.ok(response);
    }


}
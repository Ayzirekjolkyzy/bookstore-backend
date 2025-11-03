package com.okuylu_back.service;

import com.okuylu_back.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;


    @Value("${client.url}")
    private String clientUrl;

    public StripeService(@Value("${stripe.secret.key}") String stripeSecretKey, OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        Stripe.apiKey = stripeSecretKey;
    }

    private final OrderRepository orderRepository;
    public Session createCheckoutSession(Long orderId, BigDecimal amount) throws StripeException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("orderId", String.valueOf(orderId)); // это важно!

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(clientUrl + "/payment/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(clientUrl + "/payment/cancel")
                .putAllMetadata(metadata)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(amount.multiply(new BigDecimal(100)).longValue()) // Сумма в центах
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Order #" + orderId)
                                                                .setDescription("Payment for your book order")
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )

                .build();


        return Session.create(params);
    }

    public Session retrieveSession(String sessionId) throws StripeException {
        return Session.retrieve(sessionId);
    }



}
package com.okuylu_back.service;

import com.stripe.model.checkout.Session;

public interface PaymentService {


    /**
     * Обрабатывает событие от Stripe webhook
     * @param eventType Тип события (checkout.session.completed и т.д.)
     * @param session Stripe Checkout Session
     */
    void handleWebhookEvent(String eventType, Session session);
}

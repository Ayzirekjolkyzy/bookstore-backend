package com.okuylu_back.controller.user;

import com.okuylu_back.service.OrderService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/stripe")
public class WebhookController {

    private final Logger logger = LoggerFactory.getLogger(WebhookController.class);
    private final OrderService orderService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    public WebhookController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
                                                      @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }
        try {
            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
                if (session != null) {
                    String orderIdStr = session.getMetadata().get("orderId");
                    Long orderId = Long.parseLong(orderIdStr);

                    orderService.processSuccessfulPayment(orderId);
                }
            }

            return ResponseEntity.ok("Webhook received");
        } catch (Exception e) {
            logger.error("Webhook error", e);
            return ResponseEntity.status(400).body("Error");
        }
    }

}

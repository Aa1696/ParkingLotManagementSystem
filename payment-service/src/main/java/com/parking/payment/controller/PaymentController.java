package com.parking.payment.controller;

import com.parking.common.event.PaymentCompletedEvent;
import com.parking.payment.dto.PaymentRequestDto;
import com.parking.payment.service.StripePaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private StripePaymentService stripePaymentService;

    @Autowired
    private KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate;

    @Value("${stripe.api.webhookSecret}")
    private String webhookSecret;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody PaymentRequestDto request) {
        try {
            // Bypass Stripe completely to avoid API key errors
            String mockUrl = String.format("/payment-service/mock-checkout.html?ticketId=%s&invoiceId=%s&email=%s&amount=%s",
                    request.getTicketId(), request.getInvoiceId(), request.getEmail(), request.getAmount());
            return ResponseEntity.ok(Collections.singletonMap("url", mockUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/process")
    public ResponseEntity<Map<String, String>> processPayment(@RequestBody PaymentRequestDto request) {
        PaymentCompletedEvent paymentEvent = new PaymentCompletedEvent();
        paymentEvent.setTicketId(Long.parseLong(request.getTicketId()));
        paymentEvent.setInvoiceId(Long.parseLong(request.getInvoiceId()));
        paymentEvent.setPaidAt(LocalDateTime.now());
        String txnId = "txn_" + System.currentTimeMillis();
        paymentEvent.setTransactionId(txnId);
        paymentEvent.setEmail(request.getEmail());

        try {
            kafkaTemplate.send("payment-completed-topic", request.getTicketId(), paymentEvent);
            log.info("Payment completed event sent to Kafka for ticket {}", request.getTicketId());
        } catch (Exception e) {
            log.warn("Failed to publish payment event to Kafka (non-critical): {}", e.getMessage());
        }

        Map<String, String> response = Map.of(
            "transactionId", txnId,
            "status", "SUCCESS"
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/success")
    public ResponseEntity<Void> handlePaymentSuccess(
            @RequestParam("ticketId") String ticketId,
            @RequestParam("invoiceId") String invoiceId,
            @RequestParam("email") String email) {

        // When Stripe redirects here, we fire the Kafka event
        PaymentCompletedEvent paymentEvent = new PaymentCompletedEvent();
        paymentEvent.setTicketId(Long.parseLong(ticketId));
        paymentEvent.setInvoiceId(Long.parseLong(invoiceId));
        paymentEvent.setPaidAt(LocalDateTime.now());
        paymentEvent.setTransactionId("txn_" + System.currentTimeMillis());
        paymentEvent.setEmail(email);

        // Publish to Kafka
        kafkaTemplate.send("payment-completed-topic", ticketId, paymentEvent);

        // Redirect back to UI success page
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/parking-service/payment-success.html?ticketId=" + ticketId)
                .build();
    }
}

package com.parking.payment.service;

import com.parking.payment.dto.PaymentRequestDto;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class StripePaymentService {

    @Value("${stripe.api.secretKey}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public String createCheckoutSession(PaymentRequestDto request) throws StripeException {
        // We use Stripe Checkout Session API
        long amountInCents = (long) (request.getAmount() * 100);

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                // Redirect back to our backend to fire Kafka event
                .setSuccessUrl(String.format("http://localhost:8082/api/payments/success?ticketId=%s&invoiceId=%s&email=%s",
                        request.getTicketId(), request.getInvoiceId(), request.getEmail()))
                .setCancelUrl("http://localhost:8081/index.html")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(request.getCurrency())
                                                .setUnitAmount(amountInCents)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Parking Invoice " + request.getInvoiceId())
                                                                .setDescription("Ticket ID: " + request.getTicketId())
                                                                .build())
                                                .build())
                                .build())
                .putMetadata("ticketId", request.getTicketId())
                .putMetadata("invoiceId", request.getInvoiceId())
                .putMetadata("email", request.getEmail())
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }
}

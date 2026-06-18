package com.parking.notification.listener;

import com.parking.common.event.PaymentCompletedEvent;
import com.parking.common.event.TicketGeneratedEvent;
import com.parking.notification.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "ticket-generated-topic", groupId = "parking-notification-group")
    public void handleTicketGenerated(TicketGeneratedEvent event) {
        log.info("Received TicketGeneratedEvent for ticket: {}", event.getTicketId());
        
        if (event.getEmail() != null && !event.getEmail().isEmpty()) {
            String subject = "Slot Booked Successfully - Ticket #" + event.getTicketId();
            String text = String.format("Hello %s,\n\nYour vehicle (%s) has been successfully checked in.\n\n" +
                    "Ticket ID: %d\n" +
                    "Spot Number: %s\n" +
                    "Floor Number: %d\n" +
                    "Entry Time: %s\n\n" +
                    "Thank you for using Smart Parking Lot!",
                    event.getOwnerName() != null && !event.getOwnerName().isEmpty() ? event.getOwnerName() : "Customer",
                    event.getLicensePlate(),
                    event.getTicketId(),
                    event.getSpotNumber(),
                    event.getFloorNumber(),
                    event.getEntryTime().toString()
            );
            
            emailService.sendEmail(event.getEmail(), subject, text);
        } else {
            log.warn("No email provided for ticket {}. Skipping notification.", event.getTicketId());
        }
    }

    @KafkaListener(topics = "payment-completed-topic", groupId = "parking-notification-group")
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.info("Received PaymentCompletedEvent for ticket: {}", event.getTicketId());

        if (event.getEmail() != null && !event.getEmail().isEmpty()) {
            String subject = "Payment Successful - Invoice Paid";
            String text = String.format("Hello,\n\nYour payment has been received successfully.\n\n" +
                    "Ticket ID: %d\n" +
                    "Transaction ID: %s\n" +
                    "Amount Paid: $%.2f\n" +
                    "Paid At: %s\n\n" +
                    "You may now exit the parking lot. Have a safe trip!",
                    event.getTicketId(),
                    event.getTransactionId() != null ? event.getTransactionId() : "N/A",
                    event.getAmount() != null ? event.getAmount().doubleValue() : 0.0,
                    event.getPaidAt() != null ? event.getPaidAt().toString() : "N/A"
            );

            emailService.sendEmail(event.getEmail(), subject, text);
        } else {
            log.warn("No email provided for payment event for ticket {}. Skipping notification.", event.getTicketId());
        }
    }
}

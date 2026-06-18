package com.parking.service.listener;

import com.parking.common.event.PaymentCompletedEvent;
import com.parking.service.model.Invoice;
import com.parking.service.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class PaymentEventListener {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @KafkaListener(topics = "payment-completed-topic", groupId = "parking-service-group")
    @Transactional
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        if (event.getTransactionId() != null && !event.getTransactionId().isEmpty()) {
            Optional<Invoice> invoiceOpt = invoiceRepository.findByTicketId(event.getTicketId());
            invoiceOpt.ifPresent(invoice -> {
                invoice.setPaid(true);
                invoiceRepository.save(invoice);
            });
        }
    }
}

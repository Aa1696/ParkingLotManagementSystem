package com.parking.common.dto;

import com.parking.common.enums.PaymentStatus;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for payment requests between services.
 */
public class PaymentRequest implements Serializable {

    private Long invoiceId;
    private Long ticketId;
    private String licensePlate;
    private BigDecimal amount;
    private String paymentMethod; // CARD, UPI, CASH

    public PaymentRequest() {}

    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}

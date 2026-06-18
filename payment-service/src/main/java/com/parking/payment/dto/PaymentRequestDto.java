package com.parking.payment.dto;

public class PaymentRequestDto {
    private String invoiceId;
    private double amount;
    private String currency = "usd";
    private String ticketId;
    private String email;

    // Getters and Setters
    public String getInvoiceId() { return invoiceId; }
    public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

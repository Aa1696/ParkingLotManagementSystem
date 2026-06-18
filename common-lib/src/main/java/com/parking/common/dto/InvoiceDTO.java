package com.parking.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for invoice information.
 */
public class InvoiceDTO implements Serializable {

    private Long invoiceId;
    private Long ticketId;
    private String licensePlate;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private long durationMinutes;
    private BigDecimal parkingCharge;
    private BigDecimal serviceCharge;
    private BigDecimal totalAmount;
    private List<String> additionalServices;
    private String email;

    public InvoiceDTO() {}

    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }

    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }

    public long getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(long durationMinutes) { this.durationMinutes = durationMinutes; }

    public BigDecimal getParkingCharge() { return parkingCharge; }
    public void setParkingCharge(BigDecimal parkingCharge) { this.parkingCharge = parkingCharge; }

    public BigDecimal getServiceCharge() { return serviceCharge; }
    public void setServiceCharge(BigDecimal serviceCharge) { this.serviceCharge = serviceCharge; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public List<String> getAdditionalServices() { return additionalServices; }
    public void setAdditionalServices(List<String> additionalServices) { this.additionalServices = additionalServices; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

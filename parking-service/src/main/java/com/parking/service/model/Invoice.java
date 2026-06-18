package com.parking.service.model;

import com.parking.common.enums.VehicleType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Invoice entity — generated at exit gate.
 * Holds final charges, duration, and links to the original ticket.
 */
@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ticket_id", nullable = false, unique = true)
    private Ticket ticket;

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime;

    @Column(name = "exit_time", nullable = false)
    private LocalDateTime exitTime;

    @Column(name = "duration_minutes", nullable = false)
    private long durationMinutes;

    @Column(name = "parking_charge", nullable = false, precision = 10, scale = 2)
    private BigDecimal parkingCharge;

    @Column(name = "service_charge", precision = 10, scale = 2)
    private BigDecimal serviceCharge = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "paid")
    private boolean paid = false;

    @Column(name = "exit_gate")
    private int exitGate;

    public Invoice() {}

    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }

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

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public int getExitGate() { return exitGate; }
    public void setExitGate(int exitGate) { this.exitGate = exitGate; }
}

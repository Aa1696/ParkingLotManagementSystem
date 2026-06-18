package com.parking.service.model;

import com.parking.common.enums.ServiceType;
import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * AdditionalService entity — optional services (car wash, EV charging, etc.)
 * linked to a parking ticket.
 */
@Entity
@Table(name = "additional_service")
public class AdditionalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    public AdditionalService() {}

    public AdditionalService(ServiceType serviceType, BigDecimal price) {
        this.serviceType = serviceType;
        this.price = price;
    }

    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ServiceType getServiceType() { return serviceType; }
    public void setServiceType(ServiceType serviceType) { this.serviceType = serviceType; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }
}

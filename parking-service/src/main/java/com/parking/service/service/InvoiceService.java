package com.parking.service.service;

import com.parking.common.dto.InvoiceDTO;
import com.parking.common.enums.SpotStatus;
import com.parking.common.enums.TicketStatus;
import com.parking.service.model.*;
import com.parking.service.observer.ParkingObserver;
import com.parking.service.repository.*;
import com.parking.service.strategy.PricingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * InvoiceService — handles vehicle exit and invoice generation.
 * 
 * Uses PricingStrategy (Strategy Pattern) for charge calculation
 * and ParkingObserver (Observer Pattern) for capacity notifications.
 */
@Service
public class InvoiceService {

    private static final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    private final TicketService ticketService;
    private final InvoiceRepository invoiceRepository;
    private final ParkingSpotRepository spotRepository;
    private final TicketRepository ticketRepository;
    private final PricingStrategy pricingStrategy;
    private final List<ParkingObserver> observers;

    public InvoiceService(TicketService ticketService,
                          InvoiceRepository invoiceRepository,
                          ParkingSpotRepository spotRepository,
                          TicketRepository ticketRepository,
                          @Qualifier("slabPricingStrategy") PricingStrategy pricingStrategy,
                          List<ParkingObserver> observers) {
        this.ticketService = ticketService;
        this.invoiceRepository = invoiceRepository;
        this.spotRepository = spotRepository;
        this.ticketRepository = ticketRepository;
        this.pricingStrategy = pricingStrategy;
        this.observers = observers;
    }

    /**
     * Process vehicle exit.
     * 1. Validate ticket
     * 2. Calculate charges (Strategy Pattern)
     * 3. Generate invoice
     * 4. Free spot
     * 5. Notify observers (Observer Pattern)
     */
    @Transactional
    public InvoiceDTO processExit(int exitGateId, Long ticketId) {
        log.info("Processing exit at gate {} for ticket #{}", exitGateId, ticketId);

        // 1. Get and validate ticket
        Ticket ticket = ticketService.getActiveTicket(ticketId);
        LocalDateTime exitTime = LocalDateTime.now();
        long durationMinutes = Duration.between(ticket.getEntryTime(), exitTime).toMinutes();

        // 2. Calculate parking charge using Strategy Pattern
        BigDecimal parkingCharge = pricingStrategy.calculateCharge(
            durationMinutes, ticket.getVehicleType());

        // Calculate service charges
        BigDecimal serviceCharge = ticket.getAdditionalServices().stream()
            .map(AdditionalService::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAmount = parkingCharge.add(serviceCharge);

        // 3. Generate invoice
        Invoice invoice = new Invoice();
        invoice.setTicket(ticket);
        invoice.setLicensePlate(ticket.getLicensePlate());
        invoice.setEntryTime(ticket.getEntryTime());
        invoice.setExitTime(exitTime);
        invoice.setDurationMinutes(durationMinutes);
        invoice.setParkingCharge(parkingCharge);
        invoice.setServiceCharge(serviceCharge);
        invoice.setTotalAmount(totalAmount);
        invoice.setExitGate(exitGateId);
        invoice = invoiceRepository.save(invoice);

        // 4. Free the spot
        ParkingSpot spot = spotRepository.findBySpotNumber(ticket.getSpotNumber());
        Floor floor = spot.getFloor();
        spot.vacate();
        spotRepository.save(spot);

        // Update ticket status
        ticket.setStatus(TicketStatus.PAID);
        ticketRepository.save(ticket);

        // 5. Notify observers
        int total = floor.getSpots().size();
        int available = (int) floor.getSpots().stream()
            .filter(ParkingSpot::isAvailable).count();
        for (ParkingObserver observer : observers) {
            observer.onCapacityChanged(floor.getFloorNumber(), total, available);
        }

        log.info("Invoice #{} generated — total: ₹{} (parking: ₹{}, services: ₹{})",
                invoice.getId(), totalAmount, parkingCharge, serviceCharge);

        return toDTO(invoice, ticket);
    }

    private InvoiceDTO toDTO(Invoice invoice, Ticket ticket) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setInvoiceId(invoice.getId());
        dto.setTicketId(ticket.getId());
        dto.setLicensePlate(invoice.getLicensePlate());
        dto.setEntryTime(invoice.getEntryTime());
        dto.setExitTime(invoice.getExitTime());
        dto.setDurationMinutes(invoice.getDurationMinutes());
        dto.setParkingCharge(invoice.getParkingCharge());
        dto.setServiceCharge(invoice.getServiceCharge());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setEmail(ticket.getEmail());
        dto.setAdditionalServices(
            ticket.getAdditionalServices().stream()
                .map(s -> s.getServiceType().name())
                .collect(Collectors.toList())
        );
        return dto;
    }
}

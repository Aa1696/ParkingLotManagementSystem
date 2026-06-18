package com.parking.service.service;

import com.parking.common.dto.TicketDTO;
import com.parking.common.dto.VehicleEntryRequest;
import com.parking.common.enums.SpotStatus;
import com.parking.common.enums.TicketStatus;
import com.parking.common.event.TicketGeneratedEvent;
import com.parking.common.exception.SpotNotAvailableException;
import com.parking.service.factory.VehicleFactory;
import com.parking.service.model.*;
import com.parking.service.observer.ParkingObserver;
import com.parking.service.repository.*;
import com.parking.service.strategy.SpotAllocationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * TicketService — handles vehicle entry and ticket generation.
 * 
 * Orchestrates: VehicleFactory (create vehicle), SpotAllocationStrategy (find spot),
 * ParkingObserver (notify capacity change), and Kafka (publish event).
 */
@Service
public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository ticketRepository;
    private final ParkingSpotRepository spotRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleFactory vehicleFactory;
    private final SpotAllocationStrategy spotAllocationStrategy;
    private final List<ParkingObserver> observers;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public TicketService(TicketRepository ticketRepository,
                         ParkingSpotRepository spotRepository,
                         VehicleRepository vehicleRepository,
                         VehicleFactory vehicleFactory,
                         @Qualifier("nearestSpotStrategy") SpotAllocationStrategy spotAllocationStrategy,
                         List<ParkingObserver> observers,
                         KafkaTemplate<String, Object> kafkaTemplate) {
        this.ticketRepository = ticketRepository;
        this.spotRepository = spotRepository;
        this.vehicleRepository = vehicleRepository;
        this.vehicleFactory = vehicleFactory;
        this.spotAllocationStrategy = spotAllocationStrategy;
        this.observers = observers;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Process vehicle entry at a gate.
     * 1. Create or find vehicle (Factory Pattern)
     * 2. Find best available spot (Strategy Pattern)
     * 3. Generate ticket
     * 4. Notify observers (Observer Pattern)
     * 5. Publish Kafka event
     */
    @Transactional
    public TicketDTO processEntry(int gateId, VehicleEntryRequest request) {
        log.info("Processing entry at gate {} for vehicle: {} ({})",
                gateId, request.getLicensePlate(), request.getVehicleType());

        // 1. Create or find vehicle using Factory Pattern
        Vehicle vehicle = vehicleRepository.findByLicensePlate(request.getLicensePlate())
            .orElseGet(() -> {
                Vehicle newVehicle = vehicleFactory.createVehicle(
                    request.getVehicleType(),
                    request.getLicensePlate(),
                    request.getOwnerName()
                );
                return vehicleRepository.save(newVehicle);
            });

        // Check for existing active ticket
        Optional<Ticket> existingTicket = ticketRepository
            .findByLicensePlateAndStatus(request.getLicensePlate(), TicketStatus.ACTIVE);
        if (existingTicket.isPresent()) {
            throw new IllegalStateException("Vehicle " + request.getLicensePlate()
                + " already has an active ticket #" + existingTicket.get().getId());
        }

        // 2. Find best spot using Strategy Pattern
        List<ParkingSpot> availableSpots = spotRepository.findByStatus(SpotStatus.AVAILABLE);
        ParkingSpot spot = spotAllocationStrategy.findSpot(availableSpots, request.getVehicleType())
            .orElseThrow(() -> new SpotNotAvailableException(
                "No available spot for vehicle type: " + request.getVehicleType()));

        // Occupy the spot
        spot.occupy(request.getLicensePlate(), request.getVehicleType());
        spotRepository.save(spot);

        // 3. Generate ticket
        Ticket ticket = new Ticket();
        ticket.setLicensePlate(request.getLicensePlate());
        ticket.setVehicleType(request.getVehicleType());
        ticket.setOwnerName(request.getOwnerName());
        ticket.setSpotNumber(spot.getSpotNumber());
        ticket.setFloorNumber(spot.getFloor().getFloorNumber());
        ticket.setEntryGate(gateId);
        ticket.setEntryTime(LocalDateTime.now());
        ticket.setEmail(request.getEmail());
        ticket.setStatus(TicketStatus.ACTIVE);
        ticket = ticketRepository.save(ticket);

        // 4. Notify observers (Observer Pattern)
        notifyObservers(spot.getFloor());

        // 5. Publish Kafka event
        publishTicketEvent(ticket);

        log.info("Ticket #{} generated — spot {} on floor {}",
                ticket.getId(), spot.getSpotNumber(), spot.getFloor().getFloorNumber());

        return toDTO(ticket);
    }

    private void notifyObservers(Floor floor) {
        int total = floor.getSpots().size();
        int available = (int) floor.getSpots().stream()
            .filter(ParkingSpot::isAvailable).count();
        for (ParkingObserver observer : observers) {
            observer.onCapacityChanged(floor.getFloorNumber(), total, available);
        }
    }

    private void publishTicketEvent(Ticket ticket) {
        try {
            TicketGeneratedEvent event = new TicketGeneratedEvent();
            event.setTicketId(ticket.getId());
            event.setLicensePlate(ticket.getLicensePlate());
            event.setOwnerName(ticket.getOwnerName());
            event.setSpotNumber(ticket.getSpotNumber());
            event.setFloorNumber(ticket.getFloorNumber());
            event.setEntryTime(ticket.getEntryTime());
            event.setVehicleType(ticket.getVehicleType().name());
            event.setEmail(ticket.getEmail());
            kafkaTemplate.send("ticket-generated-topic", event);
        } catch (Exception e) {
            log.warn("Failed to publish ticket event to Kafka (non-critical): {}", e.getMessage());
        }
    }

    private TicketDTO toDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setTicketId(ticket.getId());
        dto.setLicensePlate(ticket.getLicensePlate());
        dto.setVehicleType(ticket.getVehicleType());
        dto.setSpotNumber(ticket.getSpotNumber());
        dto.setFloorNumber(ticket.getFloorNumber());
        dto.setGateNumber(ticket.getEntryGate());
        dto.setEntryTime(ticket.getEntryTime());
        dto.setEmail(ticket.getEmail());
        return dto;
    }

    public Ticket getActiveTicket(Long ticketId) {
        return ticketRepository.findById(ticketId)
            .filter(t -> t.getStatus() == TicketStatus.ACTIVE)
            .orElseThrow(() -> new com.parking.common.exception.InvalidTicketException(
                "No active ticket found with ID: " + ticketId));
    }
}

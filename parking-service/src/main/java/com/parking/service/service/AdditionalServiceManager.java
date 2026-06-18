package com.parking.service.service;

import com.parking.common.enums.ServiceType;
import com.parking.common.enums.VehicleType;
import com.parking.service.model.AdditionalService;
import com.parking.service.model.Ticket;
import com.parking.service.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * AdditionalServiceManager — manages optional services (car wash, EV charging, etc.)
 * with vehicle-type validation and fixed pricing.
 */
@Service
public class AdditionalServiceManager {

    private final TicketRepository ticketRepository;

    /** Pricing map for additional services. */
    private static final Map<ServiceType, BigDecimal> SERVICE_PRICES = Map.of(
        ServiceType.CAR_WASH, new BigDecimal("200.00"),
        ServiceType.EV_CHARGING, new BigDecimal("150.00"),
        ServiceType.DETAILING, new BigDecimal("500.00"),
        ServiceType.VALET, new BigDecimal("100.00")
    );

    /** Services eligible per vehicle type. */
    private static final Map<VehicleType, Set<ServiceType>> ELIGIBLE_SERVICES = Map.of(
        VehicleType.CAR, Set.of(ServiceType.CAR_WASH, ServiceType.DETAILING, ServiceType.VALET),
        VehicleType.BIKE, Set.of(ServiceType.VALET),
        VehicleType.TRUCK, Set.of(ServiceType.CAR_WASH, ServiceType.VALET),
        VehicleType.EV, Set.of(ServiceType.CAR_WASH, ServiceType.EV_CHARGING, ServiceType.DETAILING, ServiceType.VALET)
    );

    public AdditionalServiceManager(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * Add an additional service to an active ticket.
     *
     * @param ticketId    the active ticket ID
     * @param serviceType the service to add
     * @return the added service details
     */
    @Transactional
    public AdditionalService addService(Long ticketId, ServiceType serviceType) {
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + ticketId));

        // Validate service eligibility for vehicle type
        Set<ServiceType> eligible = ELIGIBLE_SERVICES.getOrDefault(
            ticket.getVehicleType(), Collections.emptySet());
        if (!eligible.contains(serviceType)) {
            throw new IllegalArgumentException(
                serviceType + " is not available for " + ticket.getVehicleType());
        }

        BigDecimal price = SERVICE_PRICES.get(serviceType);
        AdditionalService service = new AdditionalService(serviceType, price);
        ticket.addService(service);
        ticketRepository.save(ticket);

        return service;
    }

    /** List available services for a vehicle type. */
    public Map<ServiceType, BigDecimal> getAvailableServices(VehicleType vehicleType) {
        Set<ServiceType> eligible = ELIGIBLE_SERVICES.getOrDefault(vehicleType, Collections.emptySet());
        Map<ServiceType, BigDecimal> available = new LinkedHashMap<>();
        for (ServiceType type : eligible) {
            available.put(type, SERVICE_PRICES.get(type));
        }
        return available;
    }

    /** List all services with prices. */
    public Map<ServiceType, BigDecimal> getAllServices() {
        return SERVICE_PRICES;
    }
}

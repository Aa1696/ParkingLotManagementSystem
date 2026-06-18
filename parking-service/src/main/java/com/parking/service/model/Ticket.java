package com.parking.service.model;

import com.parking.common.enums.TicketStatus;
import com.parking.common.enums.VehicleType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Ticket entity — generated at entry gate.
 * Holds vehicle info, assigned spot, entry time, and any additional services.
 */
@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "email")
    private String email;

    @Column(name = "spot_number", nullable = false)
    private String spotNumber;

    @Column(name = "floor_number", nullable = false)
    private int floorNumber;

    @Column(name = "entry_gate", nullable = false)
    private int entryGate;

    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status = TicketStatus.ACTIVE;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdditionalService> additionalServices = new ArrayList<>();

    public Ticket() {}

    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSpotNumber() { return spotNumber; }
    public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }

    public int getFloorNumber() { return floorNumber; }
    public void setFloorNumber(int floorNumber) { this.floorNumber = floorNumber; }

    public int getEntryGate() { return entryGate; }
    public void setEntryGate(int entryGate) { this.entryGate = entryGate; }

    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }

    public TicketStatus getStatus() { return status; }
    public void setStatus(TicketStatus status) { this.status = status; }

    public List<AdditionalService> getAdditionalServices() { return additionalServices; }
    public void setAdditionalServices(List<AdditionalService> additionalServices) { this.additionalServices = additionalServices; }

    public void addService(AdditionalService service) {
        additionalServices.add(service);
        service.setTicket(this);
    }
}

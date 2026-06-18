package com.parking.service.model;

import com.parking.common.enums.SpotStatus;
import com.parking.common.enums.SpotType;
import com.parking.common.enums.VehicleType;
import jakarta.persistence.*;

/**
 * ParkingSpot entity — individual parking space on a floor.
 * Each spot has a type (compact, regular, large, EV) and status.
 */
@Entity
@Table(name = "parking_spot")
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spot_number", nullable = false)
    private String spotNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "spot_type", nullable = false)
    private SpotType spotType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SpotStatus status = SpotStatus.AVAILABLE;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type")
    private VehicleType vehicleType;

    @Column(name = "assigned_vehicle_plate")
    private String assignedVehiclePlate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    public ParkingSpot() {}

    public ParkingSpot(String spotNumber, SpotType spotType) {
        this.spotNumber = spotNumber;
        this.spotType = spotType;
        this.status = SpotStatus.AVAILABLE;
    }

    /** Park a vehicle in this spot. */
    public void occupy(String licensePlate, VehicleType vehicleType) {
        this.status = SpotStatus.OCCUPIED;
        this.assignedVehiclePlate = licensePlate;
        this.vehicleType = vehicleType;
    }

    /** Free this spot when a vehicle exits. */
    public void vacate() {
        this.status = SpotStatus.AVAILABLE;
        this.assignedVehiclePlate = null;
        this.vehicleType = null;
    }

    public boolean isAvailable() {
        return this.status == SpotStatus.AVAILABLE;
    }

    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSpotNumber() { return spotNumber; }
    public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }

    public SpotType getSpotType() { return spotType; }
    public void setSpotType(SpotType spotType) { this.spotType = spotType; }

    public SpotStatus getStatus() { return status; }
    public void setStatus(SpotStatus status) { this.status = status; }

    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }

    public String getAssignedVehiclePlate() { return assignedVehiclePlate; }
    public void setAssignedVehiclePlate(String assignedVehiclePlate) { this.assignedVehiclePlate = assignedVehiclePlate; }

    public Floor getFloor() { return floor; }
    public void setFloor(Floor floor) { this.floor = floor; }
}

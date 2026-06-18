package com.parking.service.model;

import com.parking.common.enums.VehicleType;
import jakarta.persistence.*;

/**
 * Vehicle entity — abstract base class.
 * Concrete types (Car, Bike, Truck, EV) are created via VehicleFactory.
 * Uses SINGLE_TABLE inheritance strategy for simplicity.
 */
@Entity
@Table(name = "vehicle")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_class", discriminatorType = DiscriminatorType.STRING)
public abstract class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @Column(name = "owner_name")
    private String ownerName;

    protected Vehicle() {}

    protected Vehicle(String licensePlate, VehicleType vehicleType, String ownerName) {
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.ownerName = ownerName;
    }

    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
}

package com.parking.service.model;

import com.parking.common.enums.VehicleType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("EV")
public class ElectricVehicle extends Vehicle {
    public ElectricVehicle() { super(); }
    public ElectricVehicle(String licensePlate, String ownerName) {
        super(licensePlate, VehicleType.EV, ownerName);
    }
}

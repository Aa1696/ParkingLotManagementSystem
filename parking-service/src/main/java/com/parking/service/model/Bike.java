package com.parking.service.model;

import com.parking.common.enums.VehicleType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BIKE")
public class Bike extends Vehicle {
    public Bike() { super(); }
    public Bike(String licensePlate, String ownerName) {
        super(licensePlate, VehicleType.BIKE, ownerName);
    }
}

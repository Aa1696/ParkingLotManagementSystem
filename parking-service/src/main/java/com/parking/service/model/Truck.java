package com.parking.service.model;

import com.parking.common.enums.VehicleType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TRUCK")
public class Truck extends Vehicle {
    public Truck() { super(); }
    public Truck(String licensePlate, String ownerName) {
        super(licensePlate, VehicleType.TRUCK, ownerName);
    }
}

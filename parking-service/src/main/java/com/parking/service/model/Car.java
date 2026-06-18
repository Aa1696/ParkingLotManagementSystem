package com.parking.service.model;

import com.parking.common.enums.VehicleType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CAR")
public class Car extends Vehicle {
    public Car() { super(); }
    public Car(String licensePlate, String ownerName) {
        super(licensePlate, VehicleType.CAR, ownerName);
    }
}

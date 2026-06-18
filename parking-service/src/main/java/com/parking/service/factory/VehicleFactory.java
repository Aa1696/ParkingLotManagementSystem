package com.parking.service.factory;

import com.parking.common.enums.VehicleType;
import com.parking.service.model.*;
import org.springframework.stereotype.Component;

/**
 * VehicleFactory — Factory Pattern.
 * 
 * Creates the appropriate Vehicle subclass based on VehicleType.
 * Encapsulates object creation, making it easy to add new vehicle types.
 */
@Component
public class VehicleFactory {

    /**
     * Create a Vehicle instance from type, license plate, and owner info.
     *
     * @param type         the vehicle type
     * @param licensePlate the vehicle's license plate
     * @param ownerName    the owner's name
     * @return a concrete Vehicle subclass instance
     * @throws IllegalArgumentException if the vehicle type is unsupported
     */
    public Vehicle createVehicle(VehicleType type, String licensePlate, String ownerName) {
        return switch (type) {
            case CAR -> new Car(licensePlate, ownerName);
            case BIKE -> new Bike(licensePlate, ownerName);
            case TRUCK -> new Truck(licensePlate, ownerName);
            case EV -> new ElectricVehicle(licensePlate, ownerName);
        };
    }
}

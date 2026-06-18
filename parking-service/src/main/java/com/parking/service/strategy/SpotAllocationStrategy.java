package com.parking.service.strategy;

import com.parking.common.enums.VehicleType;
import com.parking.service.model.ParkingSpot;

import java.util.List;
import java.util.Optional;

/**
 * SpotAllocationStrategy — Strategy Pattern interface.
 * 
 * Defines the algorithm for finding the best available parking spot.
 * Different strategies can optimize for proximity, floor preference, etc.
 */
public interface SpotAllocationStrategy {

    /**
     * Find the best available spot for the given vehicle type.
     *
     * @param availableSpots list of all available spots
     * @param vehicleType    the type of vehicle to park
     * @return the best spot, or empty if none available
     */
    Optional<ParkingSpot> findSpot(List<ParkingSpot> availableSpots, VehicleType vehicleType);

    String getStrategyName();
}

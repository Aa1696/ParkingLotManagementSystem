package com.parking.service.strategy;

import com.parking.common.enums.SpotType;
import com.parking.common.enums.VehicleType;
import com.parking.service.model.ParkingSpot;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * NearestSpotStrategy — finds the nearest available spot.
 * 
 * "Nearest" is defined as lowest floor number, then first available spot on that floor.
 * Matches vehicle type to compatible spot type.
 */
@Component("nearestSpotStrategy")
public class NearestSpotStrategy implements SpotAllocationStrategy {

    @Override
    public Optional<ParkingSpot> findSpot(List<ParkingSpot> availableSpots, VehicleType vehicleType) {
        SpotType requiredSpotType = mapVehicleToSpotType(vehicleType);

        return availableSpots.stream()
            .filter(spot -> spot.getSpotType() == requiredSpotType)
            .filter(ParkingSpot::isAvailable)
            .sorted((s1, s2) -> {
                // Sort by floor number first (nearest), then by spot number
                int floorCompare = Integer.compare(
                    s1.getFloor().getFloorNumber(),
                    s2.getFloor().getFloorNumber()
                );
                if (floorCompare != 0) return floorCompare;
                return s1.getSpotNumber().compareTo(s2.getSpotNumber());
            })
            .findFirst();
    }

    /**
     * Map vehicle type to compatible parking spot type.
     */
    private SpotType mapVehicleToSpotType(VehicleType vehicleType) {
        return switch (vehicleType) {
            case BIKE -> SpotType.COMPACT;
            case CAR -> SpotType.REGULAR;
            case TRUCK -> SpotType.LARGE;
            case EV -> SpotType.EV_CHARGING;
        };
    }

    @Override
    public String getStrategyName() {
        return "NEAREST";
    }
}

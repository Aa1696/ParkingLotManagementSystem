package com.parking.service.builder;

import com.parking.common.enums.SpotType;
import com.parking.service.model.Floor;
import com.parking.service.model.ParkingLot;
import com.parking.service.model.ParkingSpot;

/**
 * ParkingLotBuilder — Builder Pattern.
 * 
 * Constructs complex ParkingLot configurations step by step.
 * Allows flexible construction of lots with varying floors, spots, and gate counts.
 */
public class ParkingLotBuilder {

    private final ParkingLot parkingLot;
    private int spotCounter = 1;

    public ParkingLotBuilder(String name, String address) {
        this.parkingLot = new ParkingLot(name, address);
    }

    public ParkingLotBuilder withEntryGates(int count) {
        parkingLot.setTotalEntryGates(count);
        return this;
    }

    public ParkingLotBuilder withExitGates(int count) {
        parkingLot.setTotalExitGates(count);
        return this;
    }

    /**
     * Add a floor with specified number of spots per type.
     *
     * @param floorNumber  the floor number (1-indexed)
     * @param floorName    display name for the floor
     * @param compactSpots number of compact (bike) spots
     * @param regularSpots number of regular (car) spots
     * @param largeSpots   number of large (truck) spots
     * @param evSpots      number of EV charging spots
     * @return this builder for fluent chaining
     */
    public ParkingLotBuilder addFloor(int floorNumber, String floorName,
                                       int compactSpots, int regularSpots,
                                       int largeSpots, int evSpots) {
        Floor floor = new Floor(floorNumber, floorName);

        for (int i = 0; i < compactSpots; i++) {
            floor.addSpot(new ParkingSpot("F" + floorNumber + "-C" + (i + 1), SpotType.COMPACT));
        }
        for (int i = 0; i < regularSpots; i++) {
            floor.addSpot(new ParkingSpot("F" + floorNumber + "-R" + (i + 1), SpotType.REGULAR));
        }
        for (int i = 0; i < largeSpots; i++) {
            floor.addSpot(new ParkingSpot("F" + floorNumber + "-L" + (i + 1), SpotType.LARGE));
        }
        for (int i = 0; i < evSpots; i++) {
            floor.addSpot(new ParkingSpot("F" + floorNumber + "-EV" + (i + 1), SpotType.EV_CHARGING));
        }

        parkingLot.addFloor(floor);
        return this;
    }

    public ParkingLot build() {
        parkingLot.setTotalFloors(parkingLot.getFloors().size());
        return parkingLot;
    }
}

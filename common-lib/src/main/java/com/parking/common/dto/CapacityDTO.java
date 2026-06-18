package com.parking.common.dto;

import com.parking.common.enums.VehicleType;
import java.io.Serializable;

/**
 * DTO for capacity information at various granularities.
 */
public class CapacityDTO implements Serializable {

    private String level; // "LOT", "FLOOR", "VEHICLE_TYPE"
    private String identifier;
    private int totalSpots;
    private int availableSpots;
    private int occupiedSpots;
    private VehicleType vehicleType;

    public CapacityDTO() {}

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    public int getTotalSpots() { return totalSpots; }
    public void setTotalSpots(int totalSpots) { this.totalSpots = totalSpots; }

    public int getAvailableSpots() { return availableSpots; }
    public void setAvailableSpots(int availableSpots) { this.availableSpots = availableSpots; }

    public int getOccupiedSpots() { return occupiedSpots; }
    public void setOccupiedSpots(int occupiedSpots) { this.occupiedSpots = occupiedSpots; }

    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }
}

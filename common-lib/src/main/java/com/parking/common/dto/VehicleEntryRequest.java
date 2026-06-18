package com.parking.common.dto;

import com.parking.common.enums.VehicleType;
import java.io.Serializable;

/**
 * DTO for vehicle entry requests.
 */
public class VehicleEntryRequest implements Serializable {

    private String licensePlate;
    private VehicleType vehicleType;
    private String ownerName;
    private String email;

    public VehicleEntryRequest() {}

    public VehicleEntryRequest(String licensePlate, VehicleType vehicleType, String ownerName, String email) {
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.ownerName = ownerName;
        this.email = email;
    }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

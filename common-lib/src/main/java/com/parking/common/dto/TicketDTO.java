package com.parking.common.dto;

import com.parking.common.enums.VehicleType;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for ticket information.
 */
public class TicketDTO implements Serializable {

    private Long ticketId;
    private String licensePlate;
    private VehicleType vehicleType;
    private String spotNumber;
    private int floorNumber;
    private int gateNumber;
    private LocalDateTime entryTime;
    private String email;

    public TicketDTO() {}

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }

    public String getSpotNumber() { return spotNumber; }
    public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }

    public int getFloorNumber() { return floorNumber; }
    public void setFloorNumber(int floorNumber) { this.floorNumber = floorNumber; }

    public int getGateNumber() { return gateNumber; }
    public void setGateNumber(int gateNumber) { this.gateNumber = gateNumber; }

    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

package com.parking.service.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Floor entity — represents one level of the parking lot.
 * Each floor has multiple parking spots of various types.
 */
@Entity
@Table(name = "floor")
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "floor_number", nullable = false)
    private int floorNumber;

    @Column(name = "floor_name")
    private String floorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_lot_id", nullable = false)
    private ParkingLot parkingLot;

    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkingSpot> spots = new ArrayList<>();

    public Floor() {}

    public Floor(int floorNumber, String floorName) {
        this.floorNumber = floorNumber;
        this.floorName = floorName;
    }

    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getFloorNumber() { return floorNumber; }
    public void setFloorNumber(int floorNumber) { this.floorNumber = floorNumber; }

    public String getFloorName() { return floorName; }
    public void setFloorName(String floorName) { this.floorName = floorName; }

    public ParkingLot getParkingLot() { return parkingLot; }
    public void setParkingLot(ParkingLot parkingLot) { this.parkingLot = parkingLot; }

    public List<ParkingSpot> getSpots() { return spots; }
    public void setSpots(List<ParkingSpot> spots) { this.spots = spots; }

    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
        spot.setFloor(this);
    }
}

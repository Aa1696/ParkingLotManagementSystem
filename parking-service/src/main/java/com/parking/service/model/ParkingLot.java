package com.parking.service.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ParkingLot entity — implements Singleton Pattern.
 * 
 * Only one parking lot instance exists in the system.
 * Managed as a Spring @Component (singleton scope by default).
 * The database always holds exactly one row for the lot configuration.
 */
@Entity
@Table(name = "parking_lot")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(name = "total_floors")
    private int totalFloors;

    @Column(name = "total_entry_gates")
    private int totalEntryGates;

    @Column(name = "total_exit_gates")
    private int totalExitGates;

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Floor> floors = new ArrayList<>();

    public ParkingLot() {}

    public ParkingLot(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getTotalFloors() { return totalFloors; }
    public void setTotalFloors(int totalFloors) { this.totalFloors = totalFloors; }

    public int getTotalEntryGates() { return totalEntryGates; }
    public void setTotalEntryGates(int totalEntryGates) { this.totalEntryGates = totalEntryGates; }

    public int getTotalExitGates() { return totalExitGates; }
    public void setTotalExitGates(int totalExitGates) { this.totalExitGates = totalExitGates; }

    public List<Floor> getFloors() { return floors; }
    public void setFloors(List<Floor> floors) { this.floors = floors; }

    public void addFloor(Floor floor) {
        floors.add(floor);
        floor.setParkingLot(this);
    }
}

package com.parking.service.observer;

/**
 * ParkingObserver — Observer Pattern interface.
 * 
 * Observers are notified when parking capacity changes
 * (spots occupied or freed). Used for display boards,
 * analytics dashboards, etc.
 */
public interface ParkingObserver {

    /**
     * Called when the parking lot capacity changes.
     *
     * @param floorNumber    the floor where the change occurred
     * @param totalSpots     total spots on the floor
     * @param availableSpots currently available spots on the floor
     */
    void onCapacityChanged(int floorNumber, int totalSpots, int availableSpots);
}

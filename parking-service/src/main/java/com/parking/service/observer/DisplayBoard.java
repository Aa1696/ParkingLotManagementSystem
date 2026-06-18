package com.parking.service.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * DisplayBoard — Observer Pattern implementation.
 * 
 * Simulates a physical display board that shows real-time
 * parking availability per floor. In production, this could
 * push updates via WebSocket to a dashboard.
 */
@Component
public class DisplayBoard implements ParkingObserver {

    private static final Logger log = LoggerFactory.getLogger(DisplayBoard.class);

    @Override
    public void onCapacityChanged(int floorNumber, int totalSpots, int availableSpots) {
        int occupiedSpots = totalSpots - availableSpots;
        double occupancyRate = totalSpots > 0 ? (double) occupiedSpots / totalSpots * 100 : 0;

        log.info("╔══════════════════════════════════════╗");
        log.info("║       DISPLAY BOARD UPDATE           ║");
        log.info("╠══════════════════════════════════════╣");
        log.info("║  Floor: {}                           ║", floorNumber);
        log.info("║  Total Spots:     {}                 ║", totalSpots);
        log.info("║  Available Spots: {}                 ║", availableSpots);
        log.info("║  Occupied Spots:  {}                 ║", occupiedSpots);
        log.info("║  Occupancy Rate:  {:.1f}%             ║", occupancyRate);
        log.info("╚══════════════════════════════════════╝");
    }
}

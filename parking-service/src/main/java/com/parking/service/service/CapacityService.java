package com.parking.service.service;

import com.parking.common.dto.CapacityDTO;
import com.parking.common.enums.SpotStatus;
import com.parking.common.enums.SpotType;
import com.parking.common.enums.VehicleType;
import com.parking.service.repository.FloorRepository;
import com.parking.service.repository.ParkingSpotRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * CapacityService — provides real-time capacity information
 * at parking lot, floor, and vehicle-type granularity.
 */
@Service
public class CapacityService {

    private final ParkingSpotRepository spotRepository;
    private final FloorRepository floorRepository;

    public CapacityService(ParkingSpotRepository spotRepository, FloorRepository floorRepository) {
        this.spotRepository = spotRepository;
        this.floorRepository = floorRepository;
    }

    /** Lot-level capacity. */
    public CapacityDTO getLotCapacity() {
        long total = spotRepository.count();
        long available = spotRepository.countByStatus(SpotStatus.AVAILABLE);

        CapacityDTO dto = new CapacityDTO();
        dto.setLevel("LOT");
        dto.setIdentifier("main-lot");
        dto.setTotalSpots((int) total);
        dto.setAvailableSpots((int) available);
        dto.setOccupiedSpots((int) (total - available));
        return dto;
    }

    /** Floor-level capacity. */
    public CapacityDTO getFloorCapacity(int floorNumber) {
        long total = spotRepository.countByFloorNumber(floorNumber);
        long available = spotRepository.countByFloorNumberAndStatus(floorNumber, SpotStatus.AVAILABLE);

        CapacityDTO dto = new CapacityDTO();
        dto.setLevel("FLOOR");
        dto.setIdentifier("floor-" + floorNumber);
        dto.setTotalSpots((int) total);
        dto.setAvailableSpots((int) available);
        dto.setOccupiedSpots((int) (total - available));
        return dto;
    }

    /** Capacity by vehicle type. */
    public CapacityDTO getCapacityByVehicleType(VehicleType vehicleType) {
        SpotType spotType = mapVehicleToSpotType(vehicleType);
        long total = spotRepository.countBySpotType(spotType);
        long available = spotRepository.countBySpotTypeAndStatus(spotType, SpotStatus.AVAILABLE);

        CapacityDTO dto = new CapacityDTO();
        dto.setLevel("VEHICLE_TYPE");
        dto.setIdentifier(vehicleType.name());
        dto.setVehicleType(vehicleType);
        dto.setTotalSpots((int) total);
        dto.setAvailableSpots((int) available);
        dto.setOccupiedSpots((int) (total - available));
        return dto;
    }

    /** Summary of all floors. */
    public List<CapacityDTO> getAllFloorCapacities() {
        List<CapacityDTO> capacities = new ArrayList<>();
        floorRepository.findAll().forEach(floor -> {
            capacities.add(getFloorCapacity(floor.getFloorNumber()));
        });
        return capacities;
    }

    private SpotType mapVehicleToSpotType(VehicleType vehicleType) {
        return switch (vehicleType) {
            case BIKE -> SpotType.COMPACT;
            case CAR -> SpotType.REGULAR;
            case TRUCK -> SpotType.LARGE;
            case EV -> SpotType.EV_CHARGING;
        };
    }
}

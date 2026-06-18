package com.parking.service.controller;

import com.parking.common.dto.CapacityDTO;
import com.parking.common.enums.VehicleType;
import com.parking.service.service.CapacityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/capacity")
public class CapacityController {

    private final CapacityService capacityService;

    public CapacityController(CapacityService capacityService) {
        this.capacityService = capacityService;
    }

    @GetMapping("/lot")
    public ResponseEntity<CapacityDTO> getLotCapacity() {
        return ResponseEntity.ok(capacityService.getLotCapacity());
    }

    @GetMapping("/floor/{floorNumber}")
    public ResponseEntity<CapacityDTO> getFloorCapacity(@PathVariable int floorNumber) {
        return ResponseEntity.ok(capacityService.getFloorCapacity(floorNumber));
    }

    @GetMapping("/floor")
    public ResponseEntity<List<CapacityDTO>> getAllFloorCapacities() {
        return ResponseEntity.ok(capacityService.getAllFloorCapacities());
    }

    @GetMapping("/vehicle-type/{type}")
    public ResponseEntity<CapacityDTO> getCapacityByVehicleType(@PathVariable VehicleType type) {
        return ResponseEntity.ok(capacityService.getCapacityByVehicleType(type));
    }
}

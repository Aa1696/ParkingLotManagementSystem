package com.parking.service.repository;

import com.parking.service.model.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FloorRepository extends JpaRepository<Floor, Long> {
    Optional<Floor> findByFloorNumber(int floorNumber);
}

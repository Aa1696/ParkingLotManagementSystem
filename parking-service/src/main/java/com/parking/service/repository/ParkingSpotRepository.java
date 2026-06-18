package com.parking.service.repository;

import com.parking.common.enums.SpotStatus;
import com.parking.common.enums.SpotType;
import com.parking.service.model.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {

    List<ParkingSpot> findByStatus(SpotStatus status);

    List<ParkingSpot> findBySpotTypeAndStatus(SpotType spotType, SpotStatus status);

    @Query("SELECT s FROM ParkingSpot s WHERE s.floor.floorNumber = :floorNumber AND s.status = :status")
    List<ParkingSpot> findByFloorNumberAndStatus(@Param("floorNumber") int floorNumber,
                                                  @Param("status") SpotStatus status);

    @Query("SELECT COUNT(s) FROM ParkingSpot s WHERE s.status = :status")
    long countByStatus(@Param("status") SpotStatus status);

    @Query("SELECT COUNT(s) FROM ParkingSpot s WHERE s.floor.floorNumber = :floorNumber AND s.status = :status")
    long countByFloorNumberAndStatus(@Param("floorNumber") int floorNumber,
                                      @Param("status") SpotStatus status);

    @Query("SELECT COUNT(s) FROM ParkingSpot s WHERE s.spotType = :spotType AND s.status = :status")
    long countBySpotTypeAndStatus(@Param("spotType") SpotType spotType,
                                   @Param("status") SpotStatus status);

    @Query("SELECT COUNT(s) FROM ParkingSpot s WHERE s.floor.floorNumber = :floorNumber")
    long countByFloorNumber(@Param("floorNumber") int floorNumber);

    long countBySpotType(SpotType spotType);

    ParkingSpot findBySpotNumber(String spotNumber);
}

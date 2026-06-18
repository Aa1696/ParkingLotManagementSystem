package com.parking.service.strategy;

import com.parking.common.enums.VehicleType;
import java.math.BigDecimal;

/**
 * PricingStrategy — Strategy Pattern interface.
 * 
 * Different pricing strategies (slab-based, fixed, dynamic) implement this interface.
 * The strategy is selected based on configuration (weekday/weekend, vehicle type, etc.).
 */
public interface PricingStrategy {

    /**
     * Calculate the parking charge.
     *
     * @param durationMinutes how long the vehicle was parked
     * @param vehicleType     the type of vehicle
     * @return the calculated charge
     */
    BigDecimal calculateCharge(long durationMinutes, VehicleType vehicleType);

    /**
     * Name of this pricing strategy for configuration/display.
     */
    String getStrategyName();
}

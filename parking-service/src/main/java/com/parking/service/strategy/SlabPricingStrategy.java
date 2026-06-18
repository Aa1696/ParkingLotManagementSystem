package com.parking.service.strategy;

import com.parking.common.enums.VehicleType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Slab-based pricing strategy.
 * 
 * - First hour: base rate
 * - Additional hours: incremental rate per hour
 * - Rates vary by vehicle type
 */
@Component("slabPricingStrategy")
public class SlabPricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculateCharge(long durationMinutes, VehicleType vehicleType) {
        if (durationMinutes <= 0) return BigDecimal.ZERO;

        BigDecimal baseRate = getBaseRate(vehicleType);
        BigDecimal hourlyRate = getHourlyRate(vehicleType);

        // First hour is the base rate
        if (durationMinutes <= 60) {
            return baseRate;
        }

        // Additional hours at hourly rate (rounded up)
        long additionalMinutes = durationMinutes - 60;
        long additionalHours = (additionalMinutes + 59) / 60; // Ceiling division

        return baseRate.add(hourlyRate.multiply(BigDecimal.valueOf(additionalHours)));
    }

    private BigDecimal getBaseRate(VehicleType vehicleType) {
        return switch (vehicleType) {
            case BIKE -> new BigDecimal("20.00");
            case CAR -> new BigDecimal("40.00");
            case TRUCK -> new BigDecimal("60.00");
            case EV -> new BigDecimal("50.00");
        };
    }

    private BigDecimal getHourlyRate(VehicleType vehicleType) {
        return switch (vehicleType) {
            case BIKE -> new BigDecimal("10.00");
            case CAR -> new BigDecimal("20.00");
            case TRUCK -> new BigDecimal("30.00");
            case EV -> new BigDecimal("25.00");
        };
    }

    @Override
    public String getStrategyName() {
        return "SLAB";
    }
}

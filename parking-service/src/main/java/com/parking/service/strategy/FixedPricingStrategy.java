package com.parking.service.strategy;

import com.parking.common.enums.VehicleType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Fixed/Flat-rate pricing strategy.
 * 
 * Charges a flat fee per day regardless of duration (up to 24h).
 * For durations > 24h, charges per full day.
 */
@Component("fixedPricingStrategy")
public class FixedPricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculateCharge(long durationMinutes, VehicleType vehicleType) {
        if (durationMinutes <= 0) return BigDecimal.ZERO;

        BigDecimal dailyRate = getDailyRate(vehicleType);
        long days = Math.max(1, (durationMinutes + 1439) / 1440); // Ceiling to full days

        return dailyRate.multiply(BigDecimal.valueOf(days));
    }

    private BigDecimal getDailyRate(VehicleType vehicleType) {
        return switch (vehicleType) {
            case BIKE -> new BigDecimal("50.00");
            case CAR -> new BigDecimal("100.00");
            case TRUCK -> new BigDecimal("200.00");
            case EV -> new BigDecimal("150.00");
        };
    }

    @Override
    public String getStrategyName() {
        return "FIXED";
    }
}

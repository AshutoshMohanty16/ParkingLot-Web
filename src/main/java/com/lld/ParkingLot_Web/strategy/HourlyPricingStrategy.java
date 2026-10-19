package com.lld.ParkingLot_Web.strategy;

import com.lld.ParkingLot_Web.model.ParkingTicket;

import java.time.Duration;
import java.time.LocalDateTime;

public class HourlyPricingStrategy implements PricingStrategy {

    private final double hourlyRate;

    public HourlyPricingStrategy(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public double calculateFee(ParkingTicket ticket, LocalDateTime exitTime) {
        Duration duration = Duration.between(ticket.getEntryTime(), exitTime);
        if (duration.isNegative()) {
            throw new IllegalArgumentException("Exit time cannot be before entry time");
        }
        long seconds = duration.getSeconds();
        long hours = Math.max(1, (seconds + 3599) / 3600);
        return hours * hourlyRate;
    }
}

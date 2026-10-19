package com.lld.ParkingLot_Web.strategy;

import com.lld.ParkingLot_Web.model.ParkingTicket;

import java.time.LocalDateTime;

public interface PricingStrategy {
    double calculateFee(ParkingTicket ticket, LocalDateTime exitTime);
}

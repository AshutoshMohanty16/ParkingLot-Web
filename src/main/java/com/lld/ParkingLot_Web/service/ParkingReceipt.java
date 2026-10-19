package com.lld.ParkingLot_Web.service;

import java.time.LocalDateTime;

public record ParkingReceipt(
        String ticketId,
        String vehicleNumber,
        String parkingSpotId,
        LocalDateTime entryTime,
        LocalDateTime exitTime,
        double fee
) {
}

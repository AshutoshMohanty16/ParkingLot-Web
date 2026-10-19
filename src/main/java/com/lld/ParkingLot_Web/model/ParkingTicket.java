package com.lld.ParkingLot_Web.model;

import java.time.LocalDateTime;

public class ParkingTicket {
    private final String ticketId;
    private final String vehicleNumber;
    private final String parkingSpotId;
    private final LocalDateTime entryTime;

    public ParkingTicket(String ticketId, String vehicleNumber, String parkingSpotId) {
        this(ticketId, vehicleNumber, parkingSpotId, LocalDateTime.now());
    }

    public ParkingTicket(String ticketId, String vehicleNumber, String parkingSpotId, LocalDateTime entryTime) {
        this.ticketId = ticketId;
        this.vehicleNumber = vehicleNumber;
        this.parkingSpotId = parkingSpotId;
        this.entryTime = entryTime;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getParkingSpotId() {
        return parkingSpotId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }
}

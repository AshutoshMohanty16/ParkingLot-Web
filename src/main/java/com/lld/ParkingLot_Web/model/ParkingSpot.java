package com.lld.ParkingLot_Web.model;

import com.lld.ParkingLot_Web.enums.ParkingSpotType;
import com.lld.ParkingLot_Web.enums.VehicleType;

public class ParkingSpot {
    private String parkingSpotId;
    private ParkingSpotType parkingSpotType;
    private boolean isOccupied;

    public ParkingSpot(String parkingSpotId, ParkingSpotType parkingSpotType) {
        this.parkingSpotId = parkingSpotId;
        this.parkingSpotType = parkingSpotType;
        this.isOccupied = false;
    }

    // Getters and setters

    public String getParkingSpotId() {
        return parkingSpotId;
    }

    public ParkingSpotType getParkingSpotType() {
        return parkingSpotType;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public boolean canFit(VehicleType vehicle) {
        return switch (vehicle) {
            case MOTORCYCLE -> true;
            case CAR -> parkingSpotType == ParkingSpotType.MEDIUM || parkingSpotType == ParkingSpotType.LARGE;
            case TRUCK -> parkingSpotType == ParkingSpotType.LARGE;
        };
    }
}

package com.lld.ParkingLot_Web.dto;

import com.lld.ParkingLot_Web.enums.VehicleType;

public class VehicleRequest {
    private String licensePlate;
    private VehicleType type;

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }
}

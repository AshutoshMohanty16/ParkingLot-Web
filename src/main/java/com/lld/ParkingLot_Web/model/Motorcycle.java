package com.lld.ParkingLot_Web.model;

import com.lld.ParkingLot_Web.enums.VehicleType;

public class Motorcycle extends Vehicle {
    public Motorcycle(String licensePlate) {
        super(licensePlate, VehicleType.MOTORCYCLE);
    }
}

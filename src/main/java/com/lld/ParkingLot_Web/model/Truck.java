package com.lld.ParkingLot_Web.model;

import com.lld.ParkingLot_Web.enums.VehicleType;

public class Truck extends Vehicle {
    public Truck(String licensePlate) {
        super(licensePlate, VehicleType.TRUCK);
    }
}

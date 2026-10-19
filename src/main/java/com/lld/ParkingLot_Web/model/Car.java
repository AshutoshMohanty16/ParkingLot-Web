package com.lld.ParkingLot_Web.model;

import com.lld.ParkingLot_Web.enums.VehicleType;

public class Car extends Vehicle {
    public Car(String licensePlate) {
        super(licensePlate, VehicleType.CAR);
    }
}

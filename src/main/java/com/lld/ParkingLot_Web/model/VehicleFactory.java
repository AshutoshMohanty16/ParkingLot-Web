package com.lld.ParkingLot_Web.model;

import com.lld.ParkingLot_Web.enums.VehicleType;

public class VehicleFactory {
    public static Vehicle createVehicle(String licensePlate, VehicleType type) {
        if (licensePlate == null || licensePlate.isBlank()) {
            throw new IllegalArgumentException("License plate is required");
        }
        if (type == null) {
            throw new IllegalArgumentException("Vehicle type is required");
        }
        return switch (type) {
            case MOTORCYCLE -> new Motorcycle(licensePlate.trim());
            case CAR -> new Car(licensePlate.trim());
            case TRUCK -> new Truck(licensePlate.trim());
        };
    }
}

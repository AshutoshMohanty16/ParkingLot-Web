package com.lld.ParkingLot_Web.model;

import com.lld.ParkingLot_Web.enums.VehicleType;

public abstract class Vehicle {
    private final String licensePlate;
    private final VehicleType type;

    protected Vehicle(String licensePlate, VehicleType type) {
        this.licensePlate = licensePlate;
        this.type = type;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getType() {
        return type;
    }
}

//In object-oriented programming, the abstract keyword is used for Vehicle for two primary reasons: to prevent direct instantiation and to enforce a common contract/structure for all sub-types (Car, Motorcycle, Truck).
//
//Here is a breakdown of why it is used in this design:
//
//        1. Preventing Incomplete Objects (Cannot be instantiated)
//You can never do Vehicle v = new Vehicle("123", VehicleType.CAR);.
//In the real world, a generic "Vehicle" doesn't exist by itself—every vehicle must be a specific type like a Car, Motorcycle, or Truck. Marking the class as abstract enforces this rule at the compiler level. It ensures that only concrete subclasses can be created.
//
//        2. Enforcing Shared State and Behavior (Code Reusability)
//Even though you cannot instantiate a Vehicle directly, all vehicles share common properties (licensePlate, type) and common getters (getLicensePlate(), getType()).
//Instead of rewriting these fields and methods inside Car, Motorcycle, and Truck separately, the abstract class allows us to write them once and inherit them across all subclasses.
//
//        3. Establishing Polymorphism
//Because Vehicle acts as a common base type, our ParkingSpot and ParkingLot methods can accept a generic Vehicle parameter (e.g., canFitVehicle(Vehicle vehicle)). Thanks to polymorphism, you can pass a Car, Truck, or Motorcycle into that method, and Java will handle it seamlessly without needing separate methods for every single vehicle type.
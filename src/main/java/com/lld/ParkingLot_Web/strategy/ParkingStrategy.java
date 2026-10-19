package com.lld.ParkingLot_Web.strategy;

import com.lld.ParkingLot_Web.enums.VehicleType;
import com.lld.ParkingLot_Web.model.ParkingFloor;
import com.lld.ParkingLot_Web.model.ParkingSpot;

import java.util.List;
import java.util.Optional;

public interface ParkingStrategy {
    Optional<ParkingSpot> findSpot(List<ParkingFloor> floors, VehicleType vehicle);
}

//Both abstract class and interface are used to achieve abstraction in Object-Oriented Programming, but they serve completely different purposes.
//
//Here is why we use an abstract class for Vehicle versus an interface for ParkingStrategy:
//
//        1. Why use an abstract class (Vehicle)?
//For an "Is-A" Relationship & Code Sharing: A Car, Motorcycle, and Truck are all types of Vehicle. They share common state (fields) like licensePlate and type, as well as common getters.
//
//State Management: Abstract classes can have instance variables (fields) and constructors. Every vehicle needs a license plate, so the abstract class handles initializing that field so subclasses don't have to rewrite it.
//
//Partial Implementation: It allows you to write shared logic once in the parent class while leaving specific behaviors open if needed.
//
//2. Why use an interface (ParkingStrategy)?
//For a "Can-Do" Behavior Contract: An interface doesn't care what an object is; it cares about what action it can perform. ParkingStrategy represents a behavior: the ability to find a parking spot.
//
//Zero State (Pure Contract): Interfaces generally contain no instance variables and no business logic state. They only define method signatures (a contract) that any implementing class must fulfill.
//
//The Strategy Pattern (Interchangeability): By using an interface, you can completely swap out how spots are found at runtime (e.g., switching from FirstAvailableStrategy to a NearestToEntranceStrategy) without changing a single line of code in the ParkingLot class.

package com.lld.ParkingLot_Web.strategy;

import com.lld.ParkingLot_Web.enums.VehicleType;
import com.lld.ParkingLot_Web.model.ParkingFloor;
import com.lld.ParkingLot_Web.model.ParkingSpot;

import java.util.List;
import java.util.Optional;

public class FirstAvailableStrategy implements ParkingStrategy {

    @Override
    public Optional<ParkingSpot> findSpot(List<ParkingFloor> floors, VehicleType vehicle) {
        for (ParkingFloor floor : floors) {
            for (ParkingSpot spot : floor.getSpots()) {
                if (!spot.isOccupied() && spot.canFit(vehicle)) {
                    return Optional.of(spot);
                }
            }
        }
        return Optional.empty();
    }
}

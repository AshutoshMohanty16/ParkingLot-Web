package com.lld.ParkingLot_Web.strategy;

import com.lld.ParkingLot_Web.enums.ParkingSpotType;
import com.lld.ParkingLot_Web.enums.VehicleType;
import com.lld.ParkingLot_Web.model.ParkingFloor;
import com.lld.ParkingLot_Web.model.ParkingSpot;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BestFitStrategy implements ParkingStrategy {

    @Override
    public Optional<ParkingSpot> findSpot(List<ParkingFloor> floors, VehicleType vehicle) {
        return floors.stream()
                .flatMap(floor -> floor.getSpots().stream())
                .filter(spot -> !spot.isOccupied() && spot.canFit(vehicle))
                .min(Comparator.comparingInt(spot -> capacity(spot.getParkingSpotType())));
    }

    private int capacity(ParkingSpotType spotType) {
        return switch (spotType) {
            case SMALL -> 1;
            case MEDIUM -> 2;
            case LARGE -> 3;
        };
    }
}

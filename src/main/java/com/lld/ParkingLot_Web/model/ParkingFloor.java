package com.lld.ParkingLot_Web.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ParkingFloor {
    private String floorId;
    private List<ParkingSpot> spots;

    public ParkingFloor(String floorId) {
        this.floorId = floorId;
        this.spots = new ArrayList<>();
    }

    public void addSpot(ParkingSpot spot){
        spots.add(spot);
    }

}

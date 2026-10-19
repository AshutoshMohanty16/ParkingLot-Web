package com.lld.ParkingLot_Web.service;

import com.lld.ParkingLot_Web.enums.ParkingSpotType;
import com.lld.ParkingLot_Web.enums.VehicleType;
import com.lld.ParkingLot_Web.model.ParkingFloor;
import com.lld.ParkingLot_Web.model.ParkingSpot;
import com.lld.ParkingLot_Web.model.ParkingTicket;
import com.lld.ParkingLot_Web.model.Vehicle;
import com.lld.ParkingLot_Web.strategy.HourlyPricingStrategy;
import com.lld.ParkingLot_Web.strategy.ParkingStrategy;
import com.lld.ParkingLot_Web.strategy.PricingStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingService {

    private final List<ParkingFloor> parkingFloors = new ArrayList<>();
    private final Map<String, ParkingTicket> activeTickets = new HashMap<>();
    private final Map<String, ParkingSpot> occupiedSpotsMap = new HashMap<>();
    private final ParkingStrategy parkingStrategy;
    private final PricingStrategy pricingStrategy = new HourlyPricingStrategy(10.0);

    public ParkingService(ParkingStrategy parkingStrategy) {
        this.parkingStrategy = parkingStrategy;
        ParkingFloor floor = new ParkingFloor("S1");
        floor.addSpot(new ParkingSpot("F1-S1", ParkingSpotType.SMALL));
        floor.addSpot(new ParkingSpot("F1-M1", ParkingSpotType.MEDIUM));
        floor.addSpot(new ParkingSpot("F1-L1", ParkingSpotType.LARGE));
        parkingFloors.add(floor);
    }

    public synchronized ParkingTicket parkVehicle(Vehicle vehicle) {
        if (vehicle == null || vehicle.getLicensePlate() == null || vehicle.getLicensePlate().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "License plate is required");
        }
        String normalizedVehicleNumber = vehicle.getLicensePlate().trim();
        boolean alreadyParked = activeTickets.values().stream()
                .anyMatch(ticket -> ticket.getVehicleNumber().equalsIgnoreCase(normalizedVehicleNumber));
        if (alreadyParked) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle is already parked");
        }

        VehicleType vehicleType = vehicle.getType();
        if (vehicleType == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle type is required");
        }
        Optional<ParkingSpot> availableSpot = parkingStrategy.findSpot(parkingFloors, vehicleType);
        if (availableSpot.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No parking spot is available for this vehicle");
        }

        ParkingSpot spot = availableSpot.get();
        spot.setOccupied(true);
        ParkingTicket ticket = new ParkingTicket(
                UUID.randomUUID().toString(),
                normalizedVehicleNumber,
                spot.getParkingSpotId()
        );
        activeTickets.put(ticket.getTicketId(), ticket);
        occupiedSpotsMap.put(ticket.getTicketId(), spot);
        return ticket;
    }

    public synchronized ParkingReceipt unparkVehicle(String ticketId) {
        if (ticketId == null || ticketId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket ID is required");
        }

        ParkingTicket ticket = activeTickets.get(ticketId);
        if (ticket == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid or expired ticket ID");
        }
        ParkingSpot spot = occupiedSpotsMap.get(ticketId);
        if (spot == null) {
            throw new IllegalStateException("Active ticket has no associated parking spot: " + ticketId);
        }

        LocalDateTime exitTime = LocalDateTime.now();
        double fee = pricingStrategy.calculateFee(ticket, exitTime);
        spot.setOccupied(false);
        activeTickets.remove(ticketId);
        occupiedSpotsMap.remove(ticketId);
        return new ParkingReceipt(ticket.getTicketId(), ticket.getVehicleNumber(),
                ticket.getParkingSpotId(), ticket.getEntryTime(), exitTime, fee);
    }
}

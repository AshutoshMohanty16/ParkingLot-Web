package com.lld.ParkingLot_Web.controller;

import com.lld.ParkingLot_Web.dto.VehicleRequest;
import com.lld.ParkingLot_Web.model.ParkingTicket;
import com.lld.ParkingLot_Web.model.Vehicle;
import com.lld.ParkingLot_Web.model.VehicleFactory;
import com.lld.ParkingLot_Web.service.ParkingReceipt;
import com.lld.ParkingLot_Web.service.ParkingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    private final ParkingService parkingLotService;

    // Spring Dependency Injection
    public ParkingController(ParkingService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    // POST API to Park a Vehicle
    // Example JSON Body: { "licensePlate": "KA-01-AB-1234", "type": "CAR" }
    @PostMapping("/park")
    public ResponseEntity<ParkingTicket> parkVehicle(@RequestBody VehicleRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required");
        }
        if (request.getLicensePlate() == null || request.getLicensePlate().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "License plate is required");
        }
        if (request.getType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle type is required");
        }
        Vehicle vehicle = VehicleFactory.createVehicle(request.getLicensePlate(), request.getType());
        ParkingTicket ticket = parkingLotService.parkVehicle(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    // POST API to Unpark a Vehicle
    @PostMapping("/unpark/{ticketId}")
    public ResponseEntity<ParkingReceipt> unparkVehicle(@PathVariable String ticketId) {
        return ResponseEntity.ok(parkingLotService.unparkVehicle(ticketId));
    }
}

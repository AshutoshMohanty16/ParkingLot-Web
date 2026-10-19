package com.lld.ParkingLot_Web;

import com.lld.ParkingLot_Web.enums.ParkingSpotType;
import com.lld.ParkingLot_Web.enums.VehicleType;
import com.lld.ParkingLot_Web.config.ParkingStrategyConfiguration;
import com.lld.ParkingLot_Web.model.ParkingFloor;
import com.lld.ParkingLot_Web.model.ParkingSpot;
import com.lld.ParkingLot_Web.model.VehicleFactory;
import com.lld.ParkingLot_Web.service.ParkingReceipt;
import com.lld.ParkingLot_Web.service.ParkingService;
import com.lld.ParkingLot_Web.strategy.BestFitStrategy;
import com.lld.ParkingLot_Web.strategy.FirstAvailableStrategy;
import com.lld.ParkingLot_Web.strategy.WeekdayBasedStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParkingServiceTests {

    private final ParkingService parkingService = new ParkingService(new FirstAvailableStrategy());

    @Test
    void parksAndUnparksVehicleAndReleasesSpot() {
        var ticket = parkingService.parkVehicle(VehicleFactory.createVehicle("CAR-123", VehicleType.CAR));
        assertNotNull(ticket.getTicketId());
        assertEquals("F1-M1", ticket.getParkingSpotId());

        ParkingReceipt receipt = parkingService.unparkVehicle(ticket.getTicketId());
        assertEquals(ticket.getTicketId(), receipt.ticketId());
        assertEquals("CAR-123", receipt.vehicleNumber());
        assertEquals(10.0, receipt.fee(), 0.0);

        var replacementTicket = parkingService.parkVehicle(VehicleFactory.createVehicle("CAR-456", VehicleType.CAR));
        assertEquals("F1-M1", replacementTicket.getParkingSpotId());
    }

    @Test
    void rejectsDuplicateVehicleAndFullCapacity() {
        parkingService.parkVehicle(VehicleFactory.createVehicle("CAR-123", VehicleType.CAR));
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> parkingService.parkVehicle(VehicleFactory.createVehicle("car-123", VehicleType.CAR)))
                .getStatusCode().value());

        parkingService.parkVehicle(VehicleFactory.createVehicle("BIKE-123", VehicleType.MOTORCYCLE));
        parkingService.parkVehicle(VehicleFactory.createVehicle("TRUCK-123", VehicleType.TRUCK));
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> parkingService.parkVehicle(VehicleFactory.createVehicle("CAR-456", VehicleType.CAR)))
                .getStatusCode().value());
    }

    @Test
    void rejectsUnknownTicket() {
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> parkingService.unparkVehicle("unknown"))
                .getStatusCode().value());
    }

    @Test
    void bestFitStrategySelectsSmallestCompatibleSpotRegardlessOfListOrder() {
        ParkingSpot largeSpot = new ParkingSpot("LARGE", ParkingSpotType.LARGE);
        ParkingSpot mediumSpot = new ParkingSpot("MEDIUM", ParkingSpotType.MEDIUM);
        ParkingFloor floor = new ParkingFloor("F1");
        floor.addSpot(largeSpot);
        floor.addSpot(mediumSpot);

        var selectedSpot = new BestFitStrategy()
                .findSpot(List.of(floor), VehicleType.CAR)
                .orElseThrow();

        assertEquals("MEDIUM", selectedSpot.getParkingSpotId());
    }

    @Test
    void strategyConfigurationSelectsRequestedStrategy() {
        ParkingStrategyConfiguration configuration = new ParkingStrategyConfiguration();

        assertInstanceOf(BestFitStrategy.class, configuration.parkingStrategy("best-fit", "UTC"));
        assertInstanceOf(FirstAvailableStrategy.class, configuration.parkingStrategy("first-available", "UTC"));
        assertInstanceOf(WeekdayBasedStrategy.class, configuration.parkingStrategy("weekday", "UTC"));
        assertThrows(IllegalArgumentException.class,
                () -> configuration.parkingStrategy("unknown", "UTC"));
    }

    @Test
    void weekdayStrategyUsesFirstAvailableMondayThroughWednesdayAndBestFitOtherwise() {
        ParkingSpot largeSpot = new ParkingSpot("LARGE", ParkingSpotType.LARGE);
        ParkingSpot mediumSpot = new ParkingSpot("MEDIUM", ParkingSpotType.MEDIUM);
        ParkingFloor floor = new ParkingFloor("F1");
        floor.addSpot(largeSpot);
        floor.addSpot(mediumSpot);

        assertEquals("LARGE", findCarSpot(floor, "2026-10-19T12:00:00Z"));
        assertEquals("LARGE", findCarSpot(floor, "2026-10-20T12:00:00Z"));
        assertEquals("LARGE", findCarSpot(floor, "2026-10-21T12:00:00Z"));
        assertEquals("MEDIUM", findCarSpot(floor, "2026-10-22T12:00:00Z"));
        assertEquals("MEDIUM", findCarSpot(floor, "2026-10-23T12:00:00Z"));
        assertEquals("MEDIUM", findCarSpot(floor, "2026-10-24T12:00:00Z"));
        assertEquals("MEDIUM", findCarSpot(floor, "2026-10-25T12:00:00Z"));
    }

    private String findCarSpot(ParkingFloor floor, String instant) {
        WeekdayBasedStrategy strategy = new WeekdayBasedStrategy(
                new FirstAvailableStrategy(),
                new BestFitStrategy(),
                Clock.fixed(Instant.parse(instant), ZoneId.of("UTC")),
                ZoneId.of("UTC"));
        return strategy.findSpot(List.of(floor), VehicleType.CAR)
                .orElseThrow()
                .getParkingSpotId();
    }
}

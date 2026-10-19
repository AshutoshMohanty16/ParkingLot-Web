package com.lld.ParkingLot_Web.strategy;

import com.lld.ParkingLot_Web.enums.VehicleType;
import com.lld.ParkingLot_Web.model.ParkingFloor;
import com.lld.ParkingLot_Web.model.ParkingSpot;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

public class WeekdayBasedStrategy implements ParkingStrategy {

    private final ParkingStrategy firstAvailableStrategy;
    private final ParkingStrategy bestFitStrategy;
    private final Clock clock;
    private final ZoneId zoneId;

    public WeekdayBasedStrategy(
            ParkingStrategy firstAvailableStrategy,
            ParkingStrategy bestFitStrategy,
            Clock clock,
            ZoneId zoneId) {
        this.firstAvailableStrategy = firstAvailableStrategy;
        this.bestFitStrategy = bestFitStrategy;
        this.clock = clock;
        this.zoneId = zoneId;
    }

    @Override
    public Optional<ParkingSpot> findSpot(List<ParkingFloor> floors, VehicleType vehicle) {
        DayOfWeek day = LocalDate.now(clock.withZone(zoneId)).getDayOfWeek();
        ParkingStrategy strategy = switch (day) {
            case MONDAY, TUESDAY, WEDNESDAY -> firstAvailableStrategy;
            case THURSDAY, FRIDAY, SATURDAY, SUNDAY -> bestFitStrategy;
        };
        return strategy.findSpot(floors, vehicle);
    }
}

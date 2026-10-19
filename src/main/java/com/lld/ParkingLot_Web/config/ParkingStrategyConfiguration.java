package com.lld.ParkingLot_Web.config;

import com.lld.ParkingLot_Web.strategy.BestFitStrategy;
import com.lld.ParkingLot_Web.strategy.FirstAvailableStrategy;
import com.lld.ParkingLot_Web.strategy.ParkingStrategy;
import com.lld.ParkingLot_Web.strategy.WeekdayBasedStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ParkingStrategyConfiguration {

    @Bean
    public ParkingStrategy parkingStrategy(
            @Value("${parking.strategy:weekday}") String strategyName,
            @Value("${parking.strategy.time-zone:UTC}") String timeZone) {
        return switch (strategyName.trim().toLowerCase()) {
            case "first-available" -> new FirstAvailableStrategy();
            case "best-fit" -> new BestFitStrategy();
            case "weekday" -> new WeekdayBasedStrategy(
                    new FirstAvailableStrategy(),
                    new BestFitStrategy(),
                    Clock.systemUTC(),
                    ZoneId.of(timeZone));
            default -> throw new IllegalArgumentException(
                    "Unsupported parking.strategy '" + strategyName
                            + "'. Supported values are: weekday, first-available, best-fit");
        };
    }
}

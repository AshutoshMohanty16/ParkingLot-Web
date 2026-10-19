# ParkingLot-Web

Run the application with `./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`). The parking lot is in memory and starts with one small, one medium, and one large spot. Motorcycle parking can use any spot, cars use medium or large spots, and trucks use large spots. Parking costs $10 per started hour, with a one-hour minimum.

## API

### Park a vehicle

`POST /api/parking/park`

```json
{
  "licensePlate": "ABC-123",
  "type": "CAR"
}
```

The response includes a `ticketId`, vehicle number, parking spot, and entry time. Use the returned ticket ID to unpark the vehicle.

### Unpark a vehicle

`POST /api/parking/unpark/{ticketId}`

Replace `{ticketId}` with the ID returned by the park endpoint.

The response includes the parking duration timestamps and fee. The spot is freed after a successful unpark. Invalid tickets return `404`; duplicate parked vehicles and vehicles with no compatible available spot return `409`.

## Parking strategy

`ParkingService` receives its `ParkingStrategy` from Spring configuration. The default `parking.strategy=weekday` uses first-available on Monday, Tuesday, and Wednesday, and best-fit Thursday through Sunday. Set `parking.strategy.time-zone` to the timezone used to determine the current day (for example, `Asia/Kolkata`). You can override the schedule with `parking.strategy=first-available` or `parking.strategy=best-fit`. Best-fit searches all available compatible spots and chooses the smallest spot that fits, regardless of spot order. To add another policy, implement `ParkingStrategy` and register it in `ParkingStrategyConfiguration`.

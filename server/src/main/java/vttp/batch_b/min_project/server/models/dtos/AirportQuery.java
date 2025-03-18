package vttp.batch_b.min_project.server.models.dtos;

public record AirportQuery(
    String depAirport, 
    String arrAirport, 
    String depDate,
    String arrDate,
    String cabinClass,
    String tripType,
    Integer passenger
    // String depTz,
    // String arrTz
    ) 
{}

package vttp.batch_b.min_project.server.models.dtos;

import java.util.List;

import vttp.batch_b.min_project.server.models.FlightOffer;

public record CartDTO (
    String name,
    String email,
    List<FlightOffer> flights
) {}

package vttp.batch_b.min_project.server.models.dtos;

public record Country (
    String country,
    //String city,
    String timezone,
    String currency
) {}
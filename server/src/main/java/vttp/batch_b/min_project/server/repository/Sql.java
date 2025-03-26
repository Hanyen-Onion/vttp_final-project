package vttp.batch_b.min_project.server.repository;

public class Sql {
    
    //user repo
    public static final String SQL_CREATE_USER = """
        INSERT INTO users (email, username, password, country, city, timezone, currency) VALUES (?,?,?,?,?,?,?)
        """;

    public static final String SQL_SELECT_USER_BY_EMAIL = """
        SELECT email, username, password, country, city, timezone, currency FROM users WHERE email = ?
        """;

    //airport repo
    public static final String SQL_SELECT_AIRPORTS = """
        SELECT name, country, city, iata_code, icao_code, lat_decimal, lon_decimal FROM airports LIMIT ? OFFSET ?
        """;
}   

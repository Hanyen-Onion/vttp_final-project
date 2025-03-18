package vttp.batch_b.min_project.server.models;

import java.util.List;

import org.bson.Document;

public class Airport {
    private String iata;
    private String airport;
    private String country;
    private String tz;
    private String currency;
    
    public String getIata() {    return iata;}
    public void setIata(String iata) {    this.iata = iata;}
    
    public String getAirport() {    return airport;}
    public void setAirport(String airport) {    this.airport = airport;}
    
    public String getCountry() {    return country;}
    public void setCountry(String country) {    this.country = country;}
    
    public String getTz() {    return tz;}
    public void setTz(String tz) {    this.tz = tz;}
    
    public String getCurrency() {    return currency;}
    public void setCurrency(String currency) {    this.currency = currency;}

    @SuppressWarnings("unchecked")
    public static Airport docToAirport(Document doc, String airport) {
        
        Airport air = new Airport();
        air.setCountry(doc.getString("CountryName"));
        air.setIata(doc.getString("iata"));
        List<Document> airports = (List<Document>) doc.get("airports");
        airports.stream()
            .filter(airportDoc -> airportDoc.getString("name").contains(airport))
            .findFirst()
            .ifPresent(airportDoc -> {
                air.setAirport(airport);
                air.setIata(airportDoc.getString("iata"));
            });
    
        return air;
    }

    public static Document toDoc(Airport airport) {
        Document doc = new Document();

        doc.append("iata", airport.getIata())
            .append("name", airport.getAirport())
            .append("country", airport.getCountry())
            ;

        return doc;
    }

    // @Override
    // public String toString() {
    //     return "Airport [icao=" + icao + ", iata=" + iata + ", name=" + name + ", city=" + city + ", country=" + country
    //             + ", lat=" + lat + ", lon=" + lon + ", tz=" + tz + ", currency=" + currency + "]";
    // }

    
    
  

}

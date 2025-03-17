package vttp.batch_b.min_project.server.models;

import org.bson.Document;

public class Airport {
    //private String icao;
    private String iata;
    private String name;
    private String city;
    private String country;
    // private double lat;
    // private double lon;
    private String tz;
    private String currency;
    
    // public String getIcao() {    return icao;}
    // public void setIcao(String icao) {    this.icao = icao;}
    
    public String getIata() {    return iata;}
    public void setIata(String iata) {    this.iata = iata;}
    
    public String getName() {    return name;}
    public void setName(String name) {    this.name = name;}
    
    public String getCity() {    return city;}
    public void setCity(String city) {    this.city = city;}
    
    public String getCountry() {    return country;}
    public void setCountry(String country) {    this.country = country;}
    
    // public double getLat() {    return lat;}
    // public void setLat(double lat) {    this.lat = lat;}
    
    // public double getLon() {    return lon;}
    // public void setLon(double lon) {    this.lon = lon;}
    
    public String getTz() {    return tz;}
    public void setTz(String tz) {    this.tz = tz;}
    
    public String getCurrency() {    return currency;}
    public void setCurrency(String currency) {    this.currency = currency;}

    public static Airport docToAirport(Document doc) {
        
        Airport air = new Airport();
        
        
        return air;
    }

    public static Document toDoc(Airport airport) {
        Document doc = new Document();

        doc.append("iata", airport.getIata())
            //.append("icao", airport.getIcao())
            .append("name", airport.getName())
            .append("city", airport.getCity())
            .append("country", airport.getCountry())
            //.append("lat", airport.getLat())
            //.append("lon", airport.getLon())
            ;

        return doc;
    }

    // @Override
    // public String toString() {
    //     return "Airport [icao=" + icao + ", iata=" + iata + ", name=" + name + ", city=" + city + ", country=" + country
    //             + ", lat=" + lat + ", lon=" + lon + ", tz=" + tz + ", currency=" + currency + "]";
    // }

    
    
  

}

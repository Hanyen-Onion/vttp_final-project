package vttp.batch_b.min_project.server.models;

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

    public static Airport docToAirport(Document doc, String airport) {
        
        Document airDoc = (Document) doc.get("airport");
        Document curDoc = (Document) doc.get("currency");
        //Document cur2Doc = (Document) curDoc.get("currency");

        System.out.println(">>> curDoc:\n" + curDoc.toString());
        //System.out.println(">>> cur2Doc:\n" + cur2Doc.toString());
        
        Airport air = new Airport();
        air.setCountry(doc.getString("CountryName"));
        air.setIata(airDoc.getString("iata"));
        air.setCurrency(curDoc.getString("code"));
        air.setTz(doc.getString("Timezone"));

        System.out.println(air);
    
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

    @Override
    public String toString() {
        return "Airport [iata=" + iata + ", airport=" + airport + ", country=" + country + ", tz=" + tz + ", currency="
                + currency + "]";
    }

}

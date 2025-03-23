package vttp.batch_b.min_project.server.models;

import java.util.UUID;

public class FlightOffer {
    private String id;
    private String duration;
    private String depCode;
    private String depTerminal;
    private String depTime; //"2025-03-23T18:55:00"
    private String arrCode;
    private String arrTerminal;
    private String arrTime;
    private String airline;
    private String price;
    private String currency;
    //private String cabinClass;

    public FlightOffer() {
        this.id = UUID.randomUUID().toString().substring(0, 8);
    }
    
    public String getId() {    return id;}

    public String getDuration() {    return duration;}
    public void setDuration(String duration) {    this.duration = duration;}
    
    public String getDepCode() {    return depCode;}
    public void setDepCode(String depCode) {    this.depCode = depCode;}
    
    public String getDepTerminal() {    return depTerminal;}
    public void setDepTerminal(String depTerminal) {    this.depTerminal = depTerminal;}
    
    public String getDepTime() {    return depTime;}
    public void setDepTime(String depTime) {    this.depTime = depTime;}
    
    public String getArrCode() {    return arrCode;}
    public void setArrCode(String arrCode) {    this.arrCode = arrCode;}
    
    public String getArrTerminal() {    return arrTerminal;}
    public void setArrTerminal(String arrTerminal) {    this.arrTerminal = arrTerminal;}
    
    public String getArrTime() {    return arrTime;}
    public void setArrTime(String arrTime) {    this.arrTime = arrTime;}
    
    public String getAirline() {    return airline;}
    public void setAirline(String airline) {    this.airline = airline;}
    
    public String getPrice() {    return price;}
    public void setPrice(String price) {    this.price = price;}
    
    public String getCurrency() {    return currency;}
    public void setCurrency(String currency) {    this.currency = currency;}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FlightOffer{");
        sb.append("duration=").append(duration);
        sb.append(", depCode=").append(depCode);
        sb.append(", depTerminal=").append(depTerminal);
        sb.append(", depTime=").append(depTime);
        sb.append(", arrCode=").append(arrCode);
        sb.append(", arrTerminal=").append(arrTerminal);
        sb.append(", arrTime=").append(arrTime);
        sb.append(", airline=").append(airline);
        sb.append(", price=").append(price);
        sb.append(", currency=").append(currency);
        sb.append('}');
        return sb.toString();
    }
}

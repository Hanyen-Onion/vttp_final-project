package vttp.batch_b.min_project.server.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class User {
    private String username;
    private String email;
    private String password;
    private String location;
    private String timezone;
    private String currency;
    
    public String getUsername() {    return username;}
    public void setUsername(String username) {    this.username = username;}
    
    public String getEmail() {    return email;}
    public void setEmail(String email) {    this.email = email;}

    public String getPassword() {    return password;}
    public void setPassword(String password) {    this.password = password;}

    public String getLocation() {return location;}
    public void setLocation(String location) {    this.location = location;}
    
    public String getTimezone() {    return timezone;}
    public void setTimezone(String timezone) {    this.timezone = timezone;}
    
    public String getCurrency() {    return currency;}
    public void setCurrency(String currency) {    this.currency = currency;}

    public static User populate(ResultSet rs) throws SQLException {
        User u = new User();
        u.setEmail(rs.getString("email"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setLocation(rs.getString("location"));
        u.setTimezone(rs.getString("timezone"));
        u.setCurrency(rs.getString("currency"));
        return u;
    }

    public static User jsonToUser(JsonObject json) {
        User u = new User();
        u.setEmail(json.getString("email"));
        u.setUsername(json.getString("username"));
        u.setPassword(json.getString("password"));
        u.setLocation(json.getString("location"));
        u.setTimezone(json.getString("timezone"));
        u.setCurrency(json.getString("currency"));
        return u;
    }

    public static JsonObject toJson(User user) {
        JsonObject json = Json.createObjectBuilder()
            .add("email", user.getEmail())
            .add("username", user.getUsername())
            .add("location", user.getLocation())
            .add("timezone", user.getTimezone())
            .add("currency", user.getCurrency())
            .build();
        return json;
    }
    
    @Override
    public String toString() {
        return "User [username=" + username + ", email=" + email + ", password=" + password + ", location=" + location
                + ", timezone=" + timezone + ", currency=" + currency + "]";
    }
    


}

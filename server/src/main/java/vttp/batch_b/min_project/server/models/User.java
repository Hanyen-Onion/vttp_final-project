package vttp.batch_b.min_project.server.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.RowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class User {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    
    public String getUsername() {    return username;}
    public void setUsername(String username) {    this.username = username;}
    
    public String getEmail() {    return email;}
    public void setEmail(String email) {    this.email = email;}
    
    public String getFirstName() {    return firstName;}
    public void setFirstName(String firstName) {    this.firstName = firstName;}
    
    public String getLastName() {    return lastName;}
    public void setLastName(String lastName) {    this.lastName = lastName;}

    public String getPassword() {    return password;}
    public void setPassword(String password) {    this.password = password;}

    public static User populate(ResultSet rs) throws SQLException {
        User u = new User();
        u.setEmail(rs.getString("email"));
        u.setUsername(rs.getString("name"));
        u.setFirstName(rs.getString("first_name"));
        u.setLastName(rs.getString("last_name"));
        u.setPassword(rs.getString("password"));
        return u;
    }

    public static User jsonToUser(JsonObject json) {
        User u = new User();
        u.setEmail(json.getString("email"));
        u.setUsername(json.getString("name"));
        u.setFirstName(json.getString("given_name"));
        u.setLastName(json.getString("family_name"));
        return u;
    }

    public static JsonObject toJson(User user) {

        JsonObject json = Json.createObjectBuilder()
            .add("email", user.getEmail())
            .add("username", user.getUsername())
            .add("first_name", user.getFirstName())
            .add("last_name", user.getLastName())
            .add("password", user.getPassword())
            .build();
        return json;
    }
    
    @Override
    public String toString() {
        return "User:\nusername=" + username + "\nemail=" + email + "\nfirstName=" + firstName + "\nlastName=" + lastName
                + "\npassword="+ password + "\n";
    }

}

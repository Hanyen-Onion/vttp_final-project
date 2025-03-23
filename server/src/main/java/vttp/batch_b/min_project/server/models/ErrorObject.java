package vttp.batch_b.min_project.server.models;

import java.util.Date;

public class ErrorObject {
    private String message;
    private int status;
    private Date timestamp;
    //private String url;
    
    public ErrorObject(String message, int status, Date timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
        //this.url = url;
    }
    
    public String getMessage() {    return message;}
    public void setMessage(String message) {    this.message = message;}
    
    public int getStatus() {    return status;}
    public void setStatus(int status) {    this.status = status;}
    
    public Date getTimestamp() {    return timestamp;}
    public void setTimestamp(Date timestamp) {    this.timestamp = timestamp;}
    
    // public String getUrl() {    return url;}
    // public void setUrl(String url) {    this.url = url;}
}

package vttp.batch_b.min_project.server.exceptions;

public class AirportNotFoundException extends RuntimeException {

    public AirportNotFoundException() {
        super();
    }

    public AirportNotFoundException(String message) {
        super(message);
    }

    public AirportNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
}

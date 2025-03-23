package vttp.batch_b.min_project.server.exceptions;

public class EmailNotFoundException extends RuntimeException {

    public EmailNotFoundException() {
        super();
    }

    public EmailNotFoundException(String message) {
        super(message);
    }

    public EmailNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
}

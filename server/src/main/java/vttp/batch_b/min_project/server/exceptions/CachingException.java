package vttp.batch_b.min_project.server.exceptions;

public class CachingException extends RuntimeException {

    public CachingException() {
        super();
    }

    public CachingException(String message) {
        super(message);
    }

    public CachingException(String message, Throwable cause) {
        super(message, cause);
    }
    
}

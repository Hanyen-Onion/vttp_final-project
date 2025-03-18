package vttp.batch_b.min_project.server.exceptions;

public class QueryNotFoundException extends RuntimeException {

    public QueryNotFoundException() {
        super();
    }

    public QueryNotFoundException(String message) {
        super(message);
    }

    public QueryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
}

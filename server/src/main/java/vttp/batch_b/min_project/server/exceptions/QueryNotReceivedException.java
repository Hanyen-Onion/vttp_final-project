package vttp.batch_b.min_project.server.exceptions;

public class QueryNotReceivedException extends RuntimeException {

    public QueryNotReceivedException() {
        super();
    }

    public QueryNotReceivedException(String message) {
        super(message);
    }

    public QueryNotReceivedException(String message, Throwable cause) {
        super(message, cause);
    }
    
}

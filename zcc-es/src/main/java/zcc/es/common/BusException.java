package zcc.es.common;

public class BusException extends RuntimeException {
    public BusException() {
    }

    public BusException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusException(String message) {
        super(message);
    }

    public BusException(Throwable cause) {
        super(cause);
    }
}
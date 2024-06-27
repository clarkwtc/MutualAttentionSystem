package org.app.domain.exceptions;

public class RequestLimitReachedException extends CustomException{
    public RequestLimitReachedException() {
    }

    public RequestLimitReachedException(String message) {
        super(message);
    }

    public RequestLimitReachedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestLimitReachedException(Throwable cause) {
        super(cause);
    }
}

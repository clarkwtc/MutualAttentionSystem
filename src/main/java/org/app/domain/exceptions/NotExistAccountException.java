package org.app.domain.exceptions;

public class NotExistAccountException extends RuntimeException{
    public NotExistAccountException() {
    }

    public NotExistAccountException(String message) {
        super(message);
    }

    public NotExistAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistAccountException(Throwable cause) {
        super(cause);
    }
}

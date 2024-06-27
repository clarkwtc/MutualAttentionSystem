package org.app.domain.exceptions;

public class NotExistUserException extends CustomException{
    public NotExistUserException() {
    }

    public NotExistUserException(String message) {
        super(message);
    }

    public NotExistUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistUserException(Throwable cause) {
        super(cause);
    }
}

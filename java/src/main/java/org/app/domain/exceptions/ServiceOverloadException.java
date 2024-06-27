package org.app.domain.exceptions;

public class ServiceOverloadException extends CustomException{
    public ServiceOverloadException() {
    }

    public ServiceOverloadException(String message) {
        super(message);
    }

    public ServiceOverloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceOverloadException(Throwable cause) {
        super(cause);
    }
}

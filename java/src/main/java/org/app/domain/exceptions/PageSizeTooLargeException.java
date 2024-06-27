package org.app.domain.exceptions;

public class PageSizeTooLargeException extends CustomException{
    public PageSizeTooLargeException() {
    }

    public PageSizeTooLargeException(String message) {
        super(message);
    }

    public PageSizeTooLargeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PageSizeTooLargeException(Throwable cause) {
        super(cause);
    }
}

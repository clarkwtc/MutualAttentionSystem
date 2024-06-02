package org.app.domain.exceptions;

public class DuplicatedAccountException extends RuntimeException{
    public DuplicatedAccountException() {
    }

    public DuplicatedAccountException(String message) {
        super(message);
    }

    public DuplicatedAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedAccountException(Throwable cause) {
        super(cause);
    }
}

package com.nashss.se.musicplaylistservice.exceptions;

public class GoogleBookAPISearchException extends RuntimeException{
    private static final long serialVersionUID = -800081441530037536L;

    /**
     * Exception with no message or cause.
     */
    public GoogleBookAPISearchException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public GoogleBookAPISearchException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public GoogleBookAPISearchException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public GoogleBookAPISearchException(String message, Throwable cause) {
        super(message, cause);
    }
}


package com.nashss.se.musicplaylistservice.exceptions;

public class DuplicateBookException extends RuntimeException {
    private static final long serialVersionUID = -4628092263083746432L;

    /**
     * Exception with no message or cause.
     */
    public DuplicateBookException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public DuplicateBookException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public DuplicateBookException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public DuplicateBookException(String message, Throwable cause) {
        super(message, cause);
    }
}

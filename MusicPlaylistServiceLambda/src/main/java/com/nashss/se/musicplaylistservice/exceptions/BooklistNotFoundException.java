package com.nashss.se.musicplaylistservice.exceptions;
/**
 * Exception to throw when a given booklist ID is not found in the database.
 */
public class BooklistNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -5750580753817382106L;

    /**
     * Exception with no message or cause.
     */
    public BooklistNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public BooklistNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public BooklistNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public BooklistNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}


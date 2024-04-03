package com.nashss.se.musicplaylistservice.exceptions;

/**
 * Exception to throw when serializing a Book object.
 */
public class BookSerializationException extends RuntimeException {
    private static final long serialVersionUID = -70743752617795624L;

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public BookSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}

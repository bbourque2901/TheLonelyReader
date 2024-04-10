package com.nashss.se.booktrackerservice.exceptions;

public class CommentSerializationException extends RuntimeException {
    private static final long serialVersionUID = -7707127815789925026L;

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public CommentSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}

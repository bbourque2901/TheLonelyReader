package com.nashss.se.musicplaylistservice.exceptions;

public class BookSerializationException extends RuntimeException{
private static final long serialVersionUID = -70743752617795624L;
    public BookSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}

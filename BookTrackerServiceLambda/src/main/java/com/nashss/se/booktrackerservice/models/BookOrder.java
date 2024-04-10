package com.nashss.se.booktrackerservice.models;

public class BookOrder {

    public static final String DEFAULT = "DEFAULT";
    public static final String REVERSED = "REVERSED";
    public static final String SHUFFLED = "SHUFFLED";

    public static final String ALPHA = "ALPHABETICAL";

    private BookOrder() {
    }

    /**
     * Return an array of all the valid BookOrder values.
     * @return An array of BookOrder values.
     */
    public static String[] values() {
        return new String[]{DEFAULT, REVERSED, SHUFFLED, ALPHA};
    }
}

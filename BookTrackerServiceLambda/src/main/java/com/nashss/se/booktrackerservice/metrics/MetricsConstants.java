package com.nashss.se.booktrackerservice.metrics;

/**
 * Constant values for use with metrics.
 */
public class MetricsConstants {
    public static final String SERVICE = "Service";
    public static final String SERVICE_NAME = "BookTrackerService";
    public static final String NAMESPACE_NAME = "U3/BookTrackerService";
    public static final String GETBOOKLIST_BOOKLISTNOTFOUND_COUNT = "GetBooklist.BooklistNotFoundException.Count";
    public static final String UPDATEBOOKLIST_INVALIDATTRIBUTEVALUE_COUNT =
            "UpdateBooklist.InvalidAttributeValueException.Count";
    public static final String UPDATEBOOKLIST_INVALIDATTRIBUTECHANGE_COUNT =
            "UpdateBooklist.InvalidAttributeChangeException.Count";
}

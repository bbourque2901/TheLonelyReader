package com.nashss.se.musicplaylistservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

public class UpdateBookInBooklistRequest {
    private final String asin;
    private final boolean currentlyReading;
    private final int percentComplete;
    private final String customerId;

    public UpdateBookInBooklistRequest(String asin, boolean currentlyReading, int percentComplete, String customerId) {
        this.asin = asin;
        this.currentlyReading = currentlyReading;
        this.percentComplete = percentComplete;
        this.customerId = customerId;
    }

    public String getAsin() {
        return asin;
    }

    public boolean isCurrentlyReading() {
        return currentlyReading;
    }

    public int getPercentComplete() {
        return percentComplete;
    }

    public String getCustomerId() {
        return customerId;
    }

    @Override
    public String toString() {
        return "UpdateBookInBooklistRequest{" +
                "asin='" + asin + '\'' +
                ", isCurrentlyReading='" + currentlyReading + '\'' +
                ", percentComplete='" + percentComplete + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static UpdateBookInBooklistRequest.Builder builder() {
        return new UpdateBookInBooklistRequest.Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String asin;
        private boolean currentlyReading;
        private int percentComplete;
        private String customerId;
        public Builder withAsin(String asin) {
            this.asin = asin;
            return this;
        }
        public Builder withCurrentlyReading(boolean currentlyReading) {
            this.currentlyReading = currentlyReading;
            return this;
        }
        public Builder withPercentComplete(int percentComplete) {
            this.percentComplete = percentComplete;
            return this;
        }
        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }
        public UpdateBookInBooklistRequest build() { return new UpdateBookInBooklistRequest(asin, currentlyReading, percentComplete, customerId); }
    }

}

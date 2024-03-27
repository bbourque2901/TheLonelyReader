package com.nashss.se.musicplaylistservice.activity.requests;

public class GetCurrentlyReadingRequest {
    private final boolean currentlyReading;

    private GetCurrentlyReadingRequest(boolean currentlyReading) {this.currentlyReading = currentlyReading;}
    public boolean isCurrentlyReading() {return currentlyReading;}

    @Override
    public String toString() {
        return "GetCurrentlyReadingRequest{" +
                "currentlyReading='" + currentlyReading + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static GetCurrentlyReadingRequest.Builder builder() {
        return new GetCurrentlyReadingRequest.Builder();
    }

    public static class Builder {
        private boolean currentlyReading;

        public GetCurrentlyReadingRequest.Builder withCurrentlyReading(boolean currentlyReading) {
            this.currentlyReading = currentlyReading;
            return this;
        }

        public GetCurrentlyReadingRequest build() {
            return new GetCurrentlyReadingRequest(currentlyReading);
        }
    }
}

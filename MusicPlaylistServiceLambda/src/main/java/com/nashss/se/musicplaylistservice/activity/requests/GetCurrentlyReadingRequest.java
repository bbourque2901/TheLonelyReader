package com.nashss.se.musicplaylistservice.activity.requests;

public class GetCurrentlyReadingRequest {
    private final boolean currentlyReading;
    private final String id;

    private GetCurrentlyReadingRequest(boolean currentlyReading, String id) {
        this.currentlyReading = currentlyReading;
        this.id = id;
    }
    public boolean isCurrentlyReading() {
        return currentlyReading;
    }
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "GetCurrentlyReadingRequest{" +
                "currentlyReading='" + currentlyReading + '\'' +
                "id='" + id + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static GetCurrentlyReadingRequest.Builder builder() {
        return new GetCurrentlyReadingRequest.Builder();
    }

    public static class Builder {
        private boolean currentlyReading;
        private String id;

        public GetCurrentlyReadingRequest.Builder withCurrentlyReading(boolean currentlyReading) {
            this.currentlyReading = currentlyReading;
            return this;
        }

        public GetCurrentlyReadingRequest.Builder withId(String id) {
            this.id = id;
            return this;
        }

        public GetCurrentlyReadingRequest build() {
            return new GetCurrentlyReadingRequest(currentlyReading, id);
        }
    }
}

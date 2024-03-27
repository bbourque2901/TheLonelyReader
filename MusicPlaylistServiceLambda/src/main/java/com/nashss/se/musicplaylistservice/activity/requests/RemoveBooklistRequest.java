package com.nashss.se.musicplaylistservice.activity.requests;

public class RemoveBooklistRequest {

    private final String id;

    private RemoveBooklistRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "RemoveBooklistRequest{" +
                "id='" + id + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public RemoveBooklistRequest build() { return new RemoveBooklistRequest(id); }
    }
}

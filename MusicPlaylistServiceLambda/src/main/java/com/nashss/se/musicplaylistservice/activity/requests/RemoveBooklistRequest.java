package com.nashss.se.musicplaylistservice.activity.requests;

public class RemoveBooklistRequest {

    private final String id;
    private final String customerId;

    private RemoveBooklistRequest(String id, String customerId) {
        this.id = id;
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() { return customerId; }

    @Override
    public String toString() {
        return "RemoveBooklistRequest{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String customerId;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }
        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public RemoveBooklistRequest build() { return new RemoveBooklistRequest(id, customerId); }
    }
}

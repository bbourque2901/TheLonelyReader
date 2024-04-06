package com.nashss.se.musicplaylistservice.activity.requests;

public class GetUserBooklistsRequest {

    private final String customerId;

    private GetUserBooklistsRequest(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    @Override
    public String toString() {
        return "GetUserBooklistsRequest{" +
                "customerId='" + customerId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String customerId;

        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public GetUserBooklistsRequest build() {
            return new GetUserBooklistsRequest(customerId);
        }
    }
}

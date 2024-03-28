package com.nashss.se.musicplaylistservice.activity.requests;

public class RemoveBookFromBooklistRequest {

    private final String id;
    private final String asin;
    private final String customerId;

    private RemoveBookFromBooklistRequest(String id, String asin, String customerId) {
        this.id = id;
        this.asin = asin;
        this.customerId = customerId;
    }

    public String getId() { return id; }

    public String getAsin() { return asin; }

    public String getCustomerId() { return customerId; }

    @Override
    public String toString() {
        return "RemoveBookFromBooklistRequest{" +
                "id= '" + id + '\'' +
                ", asin= '" + asin + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:BUILDER
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String asin;
        private String customerid;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withAsin(String asin) {
            this.asin = asin;
            return this;
        }

        public Builder withCustomerId(String customerId) {
            this.customerid = customerId;
            return this;
        }
        public RemoveBookFromBooklistRequest build() { return new RemoveBookFromBooklistRequest(id, asin, customerid); }
    }

}

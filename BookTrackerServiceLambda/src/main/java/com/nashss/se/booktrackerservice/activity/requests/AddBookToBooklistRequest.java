package com.nashss.se.booktrackerservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = AddBookToBooklistRequest.Builder.class)
public class AddBookToBooklistRequest {

    // With the google book api, asin will represent a books asin(isbn) or search term for now
    private final String asin;
    private final String id;
    private final String customerId;

    private AddBookToBooklistRequest(String asin, String id, String customerId) {
        this.asin = asin;
        this.id = id;
        this.customerId = customerId;
    }

    public String getAsin() {
        return asin;
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    @Override
    public String toString() {
        return "AddBookToBooklistRequest{" +
                "asin='" + asin + '\'' +
                ", id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:BUILDER
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String asin;
        private String id;
        private String customerId;

        public AddBookToBooklistRequest.Builder withAsin(String asin) {
            this.asin = asin;
            return this;
        }
        public AddBookToBooklistRequest.Builder withId(String id) {
            this.id = id;
            return this;
        }
        public AddBookToBooklistRequest.Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public AddBookToBooklistRequest build() {
            return new AddBookToBooklistRequest(asin, id, customerId);
        }
    }
}

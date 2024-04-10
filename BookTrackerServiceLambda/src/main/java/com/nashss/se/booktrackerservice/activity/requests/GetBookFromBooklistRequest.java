package com.nashss.se.booktrackerservice.activity.requests;

public class GetBookFromBooklistRequest {

    private final String booklistId;
    private final String bookAsin;
    private final String customerId;

    private GetBookFromBooklistRequest(String booklistId, String bookAsin, String customerId) {
        this.booklistId = booklistId;
        this.bookAsin = bookAsin;
        this.customerId = customerId;
    }

    public String getBooklistId() {
        return booklistId;
    }

    public String getBookAsin() {
        return bookAsin;
    }

    public String getCustomerId() {
        return customerId;
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String booklistId;
        private String bookAsin;
        private String customerId;

        public Builder withBooklistId(String booklistId) {
            this.booklistId = booklistId;
            return this;
        }

        public Builder withBookAsin(String bookAsin) {
            this.bookAsin = bookAsin;
            return this;
        }

        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public GetBookFromBooklistRequest build() {
            return new GetBookFromBooklistRequest(booklistId, bookAsin, customerId);
        }
    }
}

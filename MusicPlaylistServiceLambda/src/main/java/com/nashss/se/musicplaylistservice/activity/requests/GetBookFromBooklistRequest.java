package com.nashss.se.musicplaylistservice.activity.requests;

public class GetBookFromBooklistRequest {

    private final String booklistId;
    private final String bookAsin;

    private GetBookFromBooklistRequest(String booklistId, String bookAsin) {
        this.booklistId = booklistId;
        this.bookAsin = bookAsin;
    }

    public String getBooklistId() {
        return booklistId;
    }

    public String getBookAsin() {
        return bookAsin;
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String booklistId;
        private String bookAsin;

        public Builder withBooklistId(String booklistId) {
            this.booklistId = booklistId;
            return this;
        }

        public Builder withBookAsin(String bookAsin) {
            this.bookAsin = bookAsin;
            return this;
        }

        public GetBookFromBooklistRequest build() {
            return new GetBookFromBooklistRequest(booklistId, bookAsin);
        }
    }
}

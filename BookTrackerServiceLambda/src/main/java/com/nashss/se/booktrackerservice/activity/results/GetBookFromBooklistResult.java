package com.nashss.se.booktrackerservice.activity.results;

import com.nashss.se.booktrackerservice.dynamodb.models.Book;

public class GetBookFromBooklistResult {

    private final Book book;

    private GetBookFromBooklistResult(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Book book;

        public Builder withBook(Book book) {
            this.book = book;
            return this;
        }

        public GetBookFromBooklistResult build() { return new GetBookFromBooklistResult(book); }
    }
}

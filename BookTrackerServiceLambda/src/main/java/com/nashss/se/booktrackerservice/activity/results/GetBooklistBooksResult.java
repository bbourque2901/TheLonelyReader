package com.nashss.se.booktrackerservice.activity.results;

import com.nashss.se.booktrackerservice.models.BookModel;

import java.util.ArrayList;
import java.util.List;

public class GetBooklistBooksResult {

    private final List<BookModel> books;

    private GetBooklistBooksResult(List<BookModel> books) {
        this.books = books;
    }

    public List<BookModel> getBooks() {
        return new ArrayList<>(books);
    }

    @Override
    public String toString() {
        return "GetBooklistBooksResult{" +
                "books=" + books +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<BookModel> books;

        public Builder withBooks(List<BookModel> books) {
            this.books = new ArrayList<>(books);
            return this;
        }

        public GetBooklistBooksResult build() {
            return new GetBooklistBooksResult(books);
        }
    }
}

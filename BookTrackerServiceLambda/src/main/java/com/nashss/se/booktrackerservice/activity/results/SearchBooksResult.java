package com.nashss.se.booktrackerservice.activity.results;

import com.nashss.se.booktrackerservice.models.BookModel;

import java.util.ArrayList;
import java.util.List;

public class SearchBooksResult {

    private final List<BookModel> books;

    private SearchBooksResult(List<BookModel> books) {
        this.books = books;
    }

    public List<BookModel> getBooks() {
        return new ArrayList<>(books);
    }

    @Override
    public String toString() {
        return "SearchBooksResult{" +
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

        public SearchBooksResult build() {
            return new SearchBooksResult(books);
        }
    }

}

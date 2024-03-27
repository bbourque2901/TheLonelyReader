package com.nashss.se.musicplaylistservice.activity.results;

import com.nashss.se.musicplaylistservice.models.BookModel;

import java.util.ArrayList;
import java.util.List;

public class AddBookToBooklistResult {
    private final List<BookModel> booklist;

    private AddBookToBooklistResult(List<BookModel> bookList) {
        this.booklist = bookList;
    }

    public List<BookModel> getBookList() {
        return new ArrayList<>(booklist);
    }

    @Override
    public String toString() {
        return "AddBookToBooklistResult{" +
                "bookList=" + booklist +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<BookModel> booklist;

        public Builder withBooklist(List<BookModel> booklist) {
            this.booklist = new ArrayList<>(booklist);
            return this;
        }

        public AddBookToBooklistResult build() {
            return new AddBookToBooklistResult(booklist);
        }
    }
}

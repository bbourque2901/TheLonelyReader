package com.nashss.se.musicplaylistservice.activity.results;

import com.nashss.se.musicplaylistservice.models.BookModel;

import java.util.ArrayList;
import java.util.List;

public class RemoveBookFromBooklistResult {
    private final List<BookModel> booklist;

    private RemoveBookFromBooklistResult(List<BookModel> booklist) {
        this.booklist = booklist;
    }

    public List<BookModel> getBooklist() {
        return new ArrayList<>(booklist);
    }

    @Override
    public String toString() {
        return "RemoveBookFromBooklistResult{" +
                "booklist=" + booklist +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private List<BookModel> booklist;

        public Builder withBooklist(List<BookModel> booklist) {
            this.booklist = new ArrayList<>(booklist);
            return this;
        }
        public RemoveBookFromBooklistResult build() { return new RemoveBookFromBooklistResult(booklist); }
    }
}

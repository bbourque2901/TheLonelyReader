package com.nashss.se.musicplaylistservice.activity.results;

import com.nashss.se.musicplaylistservice.models.BooklistModel;

public class RemoveBooklistResult {

    private final BooklistModel booklist;

    private RemoveBooklistResult(BooklistModel booklist) {
        this.booklist = booklist;
    }

    public BooklistModel getBooklist() {
        return booklist;
    }

    @Override
    public String toString() {
        return "RemoveBooklistResult{" +
                "booklist=" + booklist +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static RemoveBooklistResult.Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private BooklistModel booklist;

        public RemoveBooklistResult.Builder withBooklist(BooklistModel booklist) {
            this.booklist = booklist;
            return this;
        }

        public RemoveBooklistResult build() { return new RemoveBooklistResult(booklist); }
    }
}

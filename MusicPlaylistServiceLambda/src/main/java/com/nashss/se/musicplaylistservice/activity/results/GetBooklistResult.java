package com.nashss.se.musicplaylistservice.activity.results;

import com.nashss.se.musicplaylistservice.models.BooklistModel;

public class GetBooklistResult {
    private final BooklistModel booklist;

    private GetBooklistResult(BooklistModel booklist) {
        this.booklist = booklist;
    }

    public BooklistModel getBooklist() {
        return booklist;
    }

    @Override
    public String toString() {
        return "GetBooklistResult{" +
                "booklist=" + booklist +
                '}';
    }

    public static GetBooklistResult.Builder builder() {
        return new GetBooklistResult.Builder();
    }

    public static class Builder {
        private BooklistModel booklist;

        public Builder withBooklist(BooklistModel booklist) {
            this.booklist = booklist;
            return this;
        }

        public GetBooklistResult build() { return new GetBooklistResult(booklist); }
    }
}

package com.nashss.se.booktrackerservice.activity.results;

import com.nashss.se.booktrackerservice.models.BooklistModel;

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

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
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

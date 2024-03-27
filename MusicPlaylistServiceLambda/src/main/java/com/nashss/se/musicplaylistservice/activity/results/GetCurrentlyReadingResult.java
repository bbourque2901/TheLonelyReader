package com.nashss.se.musicplaylistservice.activity.results;

import com.nashss.se.musicplaylistservice.models.BooklistModel;

public class GetCurrentlyReadingResult {
    private final BooklistModel booklist;

    private GetCurrentlyReadingResult(BooklistModel booklist) {
        this.booklist = booklist;
    }

    public BooklistModel getBooklist() {
        return booklist;
    }

    @Override
    public String toString() {
        return "GetCurrentlyReadingResult{" +
                "booklist=" + booklist +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static GetBooklistResult.Builder builder() {
        return new GetBooklistResult.Builder();
    }

    public static class Builder {
        private BooklistModel booklist;

        public GetCurrentlyReadingResult.Builder withBooklist(BooklistModel booklist) {
            this.booklist = booklist;
            return this;
        }

        public GetCurrentlyReadingResult build() {
            return new GetCurrentlyReadingResult(booklist);
        }
    }
}
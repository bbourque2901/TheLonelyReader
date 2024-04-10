


package com.nashss.se.booktrackerservice.activity.results;

import com.nashss.se.booktrackerservice.models.BooklistModel;

public class CreateBooklistResult {
    private final BooklistModel booklist;

    private CreateBooklistResult(BooklistModel booklist) {
        this.booklist = booklist;
    }

    public BooklistModel getBooklist() {
        return booklist;
    }

    @Override
    public String toString() {
        return "CreateBooklistResult{" +
                "playlist=" + booklist +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static CreateBooklistResult.Builder builder() {
        return new CreateBooklistResult.Builder();
    }

    public static class Builder {
        private BooklistModel booklist;

        public CreateBooklistResult.Builder withBooklist(BooklistModel booklist) {
            this.booklist = booklist;
            return this;
        }

        public CreateBooklistResult build() {
            return new CreateBooklistResult(booklist);
        }
    }
}

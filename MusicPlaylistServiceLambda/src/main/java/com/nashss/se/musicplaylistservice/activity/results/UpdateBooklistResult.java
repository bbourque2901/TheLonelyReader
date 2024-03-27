package com.nashss.se.musicplaylistservice.activity.results;

import com.nashss.se.musicplaylistservice.models.BooklistModel;

public class UpdateBooklistResult {

    private final BooklistModel booklistModel;

    private UpdateBooklistResult(BooklistModel booklistModel) {
        this.booklistModel = booklistModel;
    }

    public BooklistModel getBooklist() {
        return booklistModel;
    }

    @Override
    public String toString() {
        return "UpdateBooklistResult{" +
                "booklist=" + booklistModel +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private BooklistModel booklistModel;

        public Builder withBooklist(BooklistModel booklistModel) {
            this.booklistModel = booklistModel;
            return this;
        }

        public UpdateBooklistResult build() {
            return new UpdateBooklistResult(booklistModel);
        }
    }
}

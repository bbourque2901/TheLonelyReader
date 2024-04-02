package com.nashss.se.musicplaylistservice.activity.results;

import com.nashss.se.musicplaylistservice.models.BookModel;

public class UpdateBookInBooklistResult {
    private final BookModel bookModel;
    private UpdateBookInBooklistResult(BookModel bookModel) { this.bookModel = bookModel; }
    public BookModel getBookModel() { return bookModel; }
    @Override
    public String toString() {
        return "UpdateBookInBooklistResult{" +
                "book=" + bookModel +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static UpdateBookInBooklistResult.Builder builder() {
        return new UpdateBookInBooklistResult.Builder();
    }

    public static class Builder {
        private BookModel bookModel;
        public Builder withBook(BookModel bookModel) {
            this.bookModel = bookModel;
            return this;
        }
        public UpdateBookInBooklistResult build() { return new UpdateBookInBooklistResult(bookModel); }
    }
}

package com.nashss.se.musicplaylistservice.models;

import java.util.Objects;

public class BookModel {

    private final String asin;
    private final String title;
    private final String author;

    private final String genre;
    private final int rating;
    private final String comments;
    private final boolean currentlyReading;
    private final int percentComplete;

    private BookModel(String asin, String title, String author, String genre, int rating
    ,String comments, boolean currentlyReading, int percentComplete) {
        this.asin = asin;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.rating = rating;
        this.comments = comments;
        this.currentlyReading = currentlyReading;
        this.percentComplete = percentComplete;
    }

    public String getAsin() {
        return asin;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() { return genre; }

    public int getRating() {
        return rating;
    }

    public String getComments() {
        return comments;
    }

    public boolean isCurrentlyReading() {
        return currentlyReading;
    }

    public int getPercentComplete() {
        return percentComplete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookModel bookModel = (BookModel) o;
        return asin.equals(bookModel.asin) &&
                title.equals(bookModel.title) &&
                author.equals(bookModel.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(asin, title, author);
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {return new Builder();}

    public static class Builder {
        private String asin;
        private String title;
        private String author;
        private String genre;
        private int rating;
        private String comments;
        private boolean currentlyReading;
        private int percentComplete;

        public Builder withAsin(String asin) {
            this.asin = asin;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withAuthor(String author) {
            this.author = author;
            return this;
        }

        public Builder withGenre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder withRating(int rating) {
            this.rating = rating;
            return this;
        }

        public Builder withComments(String comments) {
            this.comments = comments;
            return this;
        }

        public Builder withCurrentlyReading(boolean currentlyReading) {
            this.currentlyReading = currentlyReading;
            return this;
        }

        public Builder withPercentComplete(int percentComplete) {
            this.percentComplete = percentComplete;
            return this;
        }

        public BookModel build() {
            return new BookModel(asin, title, author, genre, rating, comments, currentlyReading, percentComplete);
        }
    }
}
package com.nashss.se.booktrackerservice.models;

import com.nashss.se.booktrackerservice.dynamodb.models.Comment;

import java.util.List;
import java.util.Objects;

public class BookModel {

    private final String asin;
    private final String title;
    private final String author;
    private final String genre;
    private final Integer rating;
    private final List<Comment> comments;
    private final Boolean currentlyReading;
    private final Integer percentComplete;
    private final String thumbnail;
    private final Integer pageCount;

    private BookModel(String asin, String title, String author, String genre, Integer rating,
                      List<Comment> comments, Boolean currentlyReading, Integer percentComplete,
                      String thumbnail, Integer pageCount) {
        this.asin = asin;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.rating = rating;
        this.comments = comments;
        this.currentlyReading = currentlyReading;
        this.percentComplete = percentComplete;
        this.thumbnail = thumbnail;
        this.pageCount = pageCount;
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

    public String getGenre() {
        return genre;
    }

    public Integer getRating() {
        return rating;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Boolean isCurrentlyReading() {
        return currentlyReading;
    }

    public Integer getPercentComplete() {
        return percentComplete;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Integer getPageCount() {
        return pageCount;
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
        private Integer rating;
        private List<Comment> comments;
        private Boolean currentlyReading;
        private Integer percentComplete;
        private String thumbnail;
        private Integer pageCount;

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

        public Builder withRating(Integer rating) {
            this.rating = rating;
            return this;
        }

        public Builder withComments(List<Comment> comments) {
            this.comments = comments;
            return this;
        }

        public Builder withCurrentlyReading(Boolean currentlyReading) {
            this.currentlyReading = currentlyReading;
            return this;
        }

        public Builder withPercentComplete(Integer percentComplete) {
            this.percentComplete = percentComplete;
            return this;
        }

        public Builder withThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public Builder withPageCount(Integer pageCount) {
            this.pageCount = pageCount;
            return this;
        }

        public BookModel build() {
            return new BookModel(asin, title, author, genre, rating, comments, currentlyReading, percentComplete,
                    thumbnail, pageCount);
        }
    }
}

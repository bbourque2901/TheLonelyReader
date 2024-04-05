package com.nashss.se.musicplaylistservice.models;

import java.util.Objects;

public class CommentModel {

    private final String commentId;
    private final String asin;
    private final String customerId;
    private final Integer percentComplete;
    private final String commentText;

    private CommentModel(String commentId, String asin, String customerId, Integer percentComplete,
                         String commentText) {
        this.commentId = commentId;
        this.asin = asin;
        this.customerId = customerId;
        this.percentComplete = percentComplete;
        this.commentText = commentText;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getAsin() {
        return asin;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Integer getPercentComplete() {
        return percentComplete;
    }

    public String getCommentText() {
        return commentText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentModel that = (CommentModel) o;
        return Objects.equals(commentId, that.commentId) && Objects.equals(asin, that.asin) && Objects.equals(customerId, that.customerId) && Objects.equals(percentComplete, that.percentComplete) && Objects.equals(commentText, that.commentText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, asin, customerId, percentComplete, commentText);
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {return new Builder();}

    public static class Builder {
        private String commentId;
        private String asin;
        private String customerId;
        private Integer percentComplete;
        private String commentText;

        public Builder withCommentId(String commentId) {
            this.commentId = commentId;
            return this;
        }

        public Builder withAsin(String asin) {
            this.asin = asin;
            return this;
        }

        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder withPercentComplete(Integer percentComplete) {
            this.percentComplete = percentComplete;
            return this;
        }

        public Builder withCommentText(String commentText) {
            this.commentText = commentText;
            return this;
        }

        public CommentModel build() {
            return new CommentModel(commentId, asin, customerId, percentComplete, commentText);
        }
    }
}

package com.nashss.se.musicplaylistservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = UpdateBookInBooklistRequest.Builder.class)
public class UpdateBookInBooklistRequest {
    private final String asin;
    private final boolean currentlyReading;
    private final int percentComplete;
    private final String customerId;
    private final Integer rating;
    private final String commentText;

    /**
     * Request for a new UpdateBookInBooklist object.
     *
     * @param asin asin of the book being updated
     * @param currentlyReading currentlyReading status of book
     * @param percentComplete percentComplete of book
     * @param customerId customerId of customer
     * @param rating rating of book
     * @param commentText commentText of book
     */
    public UpdateBookInBooklistRequest(String asin, boolean currentlyReading, int percentComplete, String customerId,
                                       Integer rating, String commentText) {
        this.asin = asin;
        this.currentlyReading = currentlyReading;
        this.percentComplete = percentComplete;
        this.customerId = customerId;
        this.rating = rating;
        this.commentText = commentText;
    }

    public String getAsin() {
        return asin;
    }

    public boolean isCurrentlyReading() {
        return currentlyReading;
    }

    public int getPercentComplete() {
        return percentComplete;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Integer getRating() {
        return rating;
    }
    public String getCommentText() {
        return commentText;
    }

    @Override
    public String toString() {
        return "UpdateBookInBooklistRequest{" +
                "asin='" + asin + '\'' +
                ", isCurrentlyReading='" + currentlyReading + '\'' +
                ", percentComplete='" + percentComplete + '\'' +
                ", customerId='" + customerId + '\'' +
                ", rating='" + rating + '\'' +
                ", commentText='" + commentText + '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static UpdateBookInBooklistRequest.Builder builder() {
        return new UpdateBookInBooklistRequest.Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String asin;
        private boolean currentlyReading;
        private int percentComplete;
        private String customerId;
        private Integer rating;
        private String commentText;
        public Builder withAsin(String asin) {
            this.asin = asin;
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
        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }
        public Builder withRating(Integer rating){
            this.rating = rating;
            return this;
        }
        public Builder withCommentText(String commentText) {
            this.commentText = commentText;
            return this;
        }
        public UpdateBookInBooklistRequest build() { return new UpdateBookInBooklistRequest(asin, currentlyReading, percentComplete, customerId, rating, commentText); }
    }

}

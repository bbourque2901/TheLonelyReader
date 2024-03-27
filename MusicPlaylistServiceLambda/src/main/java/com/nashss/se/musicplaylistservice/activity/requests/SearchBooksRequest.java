package com.nashss.se.musicplaylistservice.activity.requests;

public class SearchBooksRequest {

    private final String criteria;

    private SearchBooksRequest(String criteria) {
        this.criteria = criteria;
    }

    public String getCriteria() {
        return criteria;
    }

    @Override
    public String toString() {
        return "SearchBooksRequest{" +
                "criteria='" + criteria + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String criteria;
        public Builder withCriteria(String criteria) {
            this.criteria = criteria;
            return this;
        }

        public SearchBooksRequest build() {
            return new SearchBooksRequest(criteria);
        }
    }

}

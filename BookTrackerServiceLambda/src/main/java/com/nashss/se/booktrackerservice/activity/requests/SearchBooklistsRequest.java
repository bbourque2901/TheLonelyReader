package com.nashss.se.booktrackerservice.activity.requests;

public class SearchBooklistsRequest {

    private final String criteria;

    private SearchBooklistsRequest(String criteria) {
        this.criteria = criteria;
    }

    public String getCriteria() {
        return criteria;
    }

    @Override
    public String toString() {
        return "SearchBooklistsRequest{" +
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

        public SearchBooklistsRequest build() {
            return new SearchBooklistsRequest(criteria);
        }
    }
}

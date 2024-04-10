package com.nashss.se.booktrackerservice.activity.requests;

public class GetBooklistRequest {

    private final String id;

    private GetBooklistRequest(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "GetBooklistRequest{" +
                "id='" + id + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public GetBooklistRequest build() {
            return new GetBooklistRequest(id);
        }
    }
}

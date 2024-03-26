package com.nashss.se.musicplaylistservice.activity.requests;

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

    public static GetBooklistRequest.Builder builder() {
        return new GetBooklistRequest.Builder();
    }

    public static class Builder {
        private String id;

        public GetBooklistRequest.Builder withId(String id) {
            this.id = id;
            return this;
        }

        public GetBooklistRequest build() {
            return new GetBooklistRequest(id);
        }
    }
}

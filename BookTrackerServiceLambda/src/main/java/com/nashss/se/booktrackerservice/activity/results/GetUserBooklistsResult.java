package com.nashss.se.booktrackerservice.activity.results;

import com.nashss.se.booktrackerservice.models.BooklistModel;

import java.util.List;

public class GetUserBooklistsResult {

    private final List<BooklistModel> booklists;

    private GetUserBooklistsResult(List<BooklistModel> booklists) {
        this.booklists = booklists;
    }

    public List<BooklistModel> getBooklists() {
        return booklists;
    }

    @Override
    public String toString() {
        return "GetUserBooklistsResult{" +
                "booklists=" + booklists +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<BooklistModel> booklists;

        public Builder withBooklists(List<BooklistModel> booklists) {
            this.booklists = booklists;
            return this;
        }

        public GetUserBooklistsResult build() { return new GetUserBooklistsResult(booklists); }
    }

}

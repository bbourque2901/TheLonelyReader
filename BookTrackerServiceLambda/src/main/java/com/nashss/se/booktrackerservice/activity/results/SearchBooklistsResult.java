package com.nashss.se.booktrackerservice.activity.results;

import com.nashss.se.booktrackerservice.models.BooklistModel;

import java.util.ArrayList;
import java.util.List;

public class SearchBooklistsResult {

    private final List<BooklistModel> booklists;

    private SearchBooklistsResult(List<BooklistModel> booklists) {
        this.booklists = booklists;
    }

    public List<BooklistModel> getBooklists() {
        return booklists;
    }

    @Override
    public String toString() {
        return "SearchBooklistsResult{" +
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
            this.booklists = new ArrayList<>(booklists);
            return this;
        }

        public SearchBooklistsResult build() {
            return new SearchBooklistsResult(booklists);
        }
    }
}

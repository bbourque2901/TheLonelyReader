package com.nashss.se.musicplaylistservice.models;

import java.util.List;
import java.util.Objects;

import static com.nashss.se.musicplaylistservice.utils.CollectionUtils.copyToList;

public class BooklistModel {
    private final String id;
    private final String name;
    private final String customerId;
    private final int bookCount;
    private final List<String> tags;

    private BooklistModel(String id, String name, String customerId, int bookCount, List<String> tags) {
        this.id = id;
        this.name = name;
        this.customerId = customerId;
        this.bookCount = bookCount;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public int getBookCount() {
        return bookCount;
    }

    public List<String> getTags() {
        return copyToList(tags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BooklistModel that = (BooklistModel) o;
        return bookCount == that.bookCount && Objects.equals(id, that.id) && Objects.equals(name, that.name) &&
                Objects.equals(customerId, that.customerId) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, customerId, bookCount, tags);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private String customerId;
        private int bookCount;
        private List<String> tags;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder withBookCount(int bookCount) {
            this.bookCount = bookCount;
            return this;
        }

        public Builder withTags(List<String> tags) {
            this.tags = copyToList(tags);
            return this;
        }

        public BooklistModel build() {
            return new BooklistModel(id, name, customerId, bookCount, tags);
        }
    }
}

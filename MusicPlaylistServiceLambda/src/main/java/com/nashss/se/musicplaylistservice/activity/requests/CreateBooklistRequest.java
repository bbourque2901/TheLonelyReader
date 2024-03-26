package com.nashss.se.musicplaylistservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;

import static com.nashss.se.musicplaylistservice.utils.CollectionUtils.copyToList;

@JsonDeserialize(builder = CreateBooklistRequest.Builder.class)
public class CreateBooklistRequest {
    private final String name;
    private final String customerId;
    private final List<String> tags;

    private CreateBooklistRequest(String name, String customerId, List<String> tags) {
        this.name = name;
        this.customerId = customerId;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<String> getTags() {
        return copyToList(tags);
    }

    @Override
    public String toString() {
        return "CreateBooklistRequest{" +
                "name='" + name + '\'' +
                ", customerId='" + customerId + '\'' +
                ", tags=" + tags +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static CreateBooklistRequest.Builder builder() {
        return new CreateBooklistRequest.Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String name;
        private String customerId;
        private List<String> tags;

        public CreateBooklistRequest.Builder withName(String name) {
            this.name = name;
            return this;
        }

        public CreateBooklistRequest.Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public CreateBooklistRequest.Builder withTags(List<String> tags) {
            this.tags = copyToList(tags);
            return this;
        }

        public CreateBooklistRequest build() {
            return new CreateBooklistRequest(name, customerId, tags);
        }
    }
}

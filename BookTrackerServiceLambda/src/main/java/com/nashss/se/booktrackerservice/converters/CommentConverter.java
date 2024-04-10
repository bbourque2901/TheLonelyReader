package com.nashss.se.booktrackerservice.converters;

import com.nashss.se.booktrackerservice.dynamodb.models.Comment;
import com.nashss.se.booktrackerservice.exceptions.CommentSerializationException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class CommentConverter implements DynamoDBTypeConverter<String, List<Comment>> {

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convert(List<Comment> object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new CommentSerializationException("Comment failed to deserialize", e);
        }
    }

    @Override
    public List<Comment> unconvert(String object) {
        TypeReference<List<Comment>> ref = new TypeReference<List<Comment>>() {

        };
        try {
            return mapper.readValue(object, ref);
        } catch (JsonProcessingException e) {
            throw new CommentSerializationException("Comment failed to be created", e);
        }
    }
}

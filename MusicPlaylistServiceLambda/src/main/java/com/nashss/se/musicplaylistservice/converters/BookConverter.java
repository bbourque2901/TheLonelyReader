package com.nashss.se.musicplaylistservice.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashss.se.musicplaylistservice.dynamodb.models.Book;
import com.nashss.se.musicplaylistservice.exceptions.BookSerializationException;

import java.util.List;

public class BookConverter implements DynamoDBTypeConverter<String, List<Book>> {
    ObjectMapper mapper = new ObjectMapper();
    @Override
    public String convert(List<Book> object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new BookSerializationException("Book failed to deserialize", e);
        }
    }

    @Override
    public List<Book> unconvert(String object) {
        TypeReference<List<Book>> ref = new TypeReference<List<Book>>() {
        };
        try {
            return mapper.readValue(object, ref);
        } catch (JsonProcessingException e) {
            throw new BookSerializationException("Book failed to be created", e);
        }
    }
}

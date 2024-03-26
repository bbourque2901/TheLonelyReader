package com.nashss.se.musicplaylistservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.musicplaylistservice.dynamodb.models.Book;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses data for a book using {@link Book} to represent the model in DynamoDB.
 */

@Singleton
public class BookDao {

    private final DynamoDBMapper dynamoDBMapper;

    /**
     * Instantiates an BookDao object.
     *
     * @param dynamoDbMapper the {@link DynamoDBMapper} used to interact with the book_table table
     */

    @Inject
    public BookDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDBMapper = dynamoDbMapper;
    }
}

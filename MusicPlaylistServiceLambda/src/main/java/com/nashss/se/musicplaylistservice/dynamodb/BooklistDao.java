package com.nashss.se.musicplaylistservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses data for a booklist using {@link Booklist} to represent the model in DynamoDB.
 */
@Singleton
public class BooklistDao {
    private final DynamoDBMapper dynamoDBMapper;

    /**
     * Instantiates a BooklistDao object.
     *
     * @param dynamoDbMapper the {@link DynamoDBMapper} used to interact with the booklists table
     */

    @Inject
    public BooklistDao(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }
}

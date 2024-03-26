package com.nashss.se.musicplaylistservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;

import com.nashss.se.musicplaylistservice.exceptions.BooklistNotFoundException;


import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses data for a booklist using {@link com.nashss.se.musicplaylistservice.dynamodb.models.Booklist} to represent the model in DynamoDB.
 */
@Singleton
public class BooklistDao {
    private final DynamoDBMapper dynamoDBMapper;

    /**
     * Instantiates a BooklistDao object.
     *
     * @param dynamoDBMapper the {@link DynamoDBMapper} used to interact with the booklists table
     */

    @Inject
    public BooklistDao(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public Booklist saveBooklist(Booklist booklist) {
        this.dynamoDBMapper.save(booklist);
        return booklist;
    }

    /**
     * Returns the {@link Booklist} corresponding to the specified id.
     *
     * @param id the Booklist ID
     * @return the stored Booklist, or null if none was found.
     */
    public Booklist getBooklist(String id) {
        Booklist booklist = this.dynamoDBMapper.load(Booklist.class, id);

        if (booklist == null) {
            throw new BooklistNotFoundException("Could not find booklist with id " + id);
        }

        return booklist;
    }
}

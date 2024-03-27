package com.nashss.se.musicplaylistservice.dynamodb;

import com.nashss.se.musicplaylistservice.dynamodb.models.Book;
import com.nashss.se.musicplaylistservice.exceptions.BookNotFoundException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

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

    /**
     * Returns the {@link Book} corresponding to the specified asin.
     *
     * @param asin the Book asin
     * @return the stored Book, or null if none was found.
     */
    public Book getBook(String asin) {
        Book book = this.dynamoDBMapper.load(Book.class, asin);
        if (book == null) {
            throw new BookNotFoundException("Could not find book with asin: " + asin);
        }

        return book;
    }

}

package com.nashss.se.musicplaylistservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.ScanResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.nashss.se.musicplaylistservice.dynamodb.models.Book;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * Perform a search (via a "scan") of the book table for book with currentlyReading set to true.
     *
     * CurrentlyReading attribute is searched.
     *
     * @return a List of Booklist objects that match the search criteria.
     */
    public Booklist getCurrentlyReading(boolean isCurrentlyReading) {
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":currentlyReading", new AttributeValue().withBOOL(isCurrentlyReading));
        DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression()
                .withFilterExpression("currentlyReading = :currentlyReading")
                .withExpressionAttributeValues(valueMap);
        ScanResultPage<Book> bookResultPage = dynamoDBMapper.scanPage(Book.class, dynamoDBScanExpression);
        List<Book> results = bookResultPage.getResults();
        List<String> asins = new ArrayList<>();
        for (Book book : results) {
            asins.add(book.getAsin());
        }
        Booklist returnList = new Booklist();
        returnList.setAsins(asins);
        returnList.setBookCount(asins.size());
        returnList.setName("Currently Reading");
        return returnList;
    }
}

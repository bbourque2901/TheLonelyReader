package com.nashss.se.musicplaylistservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;

import com.nashss.se.musicplaylistservice.exceptions.BooklistNotFoundException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Saves (creates or updates) the given booklist.
     *
     * @param booklist The booklist to save
     * @return The Booklist object that was saved
     */
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

    /**
     * Perform a search (via a "scan") of the booklist table for booklists matching the given criteria.
     *
     * Both "booklistName" and "tags" attributes are searched.
     * The criteria are an array of Strings. Each element of the array is search individually.
     * ALL elements of the criteria array must appear in the booklistName or the tags (or both).
     * Searches are CASE SENSITIVE.
     *
     * @param criteria an array of String containing search criteria.
     * @return a List of Booklist objects that match the search criteria.
     */
    public List<Booklist> searchBooklists(String[] criteria) {
        DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression();

        if (criteria.length > 0) {
            Map<String, AttributeValue> valueMap = new HashMap<>();
            String valueMapNamePrefix = ":c";

            StringBuilder nameFilterExpression = new StringBuilder();
            StringBuilder tagsFilterExpression = new StringBuilder();

            for (int i = 0; i < criteria.length; i++) {
                valueMap.put(valueMapNamePrefix + i,
                        new AttributeValue().withS(criteria[i]));
                nameFilterExpression.append(
                        filterExpressionPart("booklistName", valueMapNamePrefix, i));
                tagsFilterExpression.append(
                        filterExpressionPart("tags", valueMapNamePrefix, i));
            }

            dynamoDBScanExpression.setExpressionAttributeValues(valueMap);
            dynamoDBScanExpression.setFilterExpression(
                    "(" + nameFilterExpression + ") or (" + tagsFilterExpression + ")");
        }

        return this.dynamoDBMapper.scan(Booklist.class, dynamoDBScanExpression);
    }

    /**
     * Helper method for searchBooklists method
     */
    private StringBuilder filterExpressionPart(String target, String valueMapNamePrefix, int position) {
        String possiblyAnd = position == 0 ? "" : "and ";
        return new StringBuilder()
                .append(possiblyAnd)
                .append("contains(")
                .append(target)
                .append(", ")
                .append(valueMapNamePrefix).append(position)
                .append(") ");
    }
}

package com.nashss.se.booktrackerservice.dynamodb;

import com.nashss.se.booktrackerservice.dynamodb.models.Comment;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses data for a comment using {@link Comment} to represent the model in DynamoDB.
 */
@Singleton
public class CommentDao {

    private final DynamoDBMapper dynamoDBMapper;

    /**
     * Instantiates an CommentDao object.
     *
     * @param dynamoDBMapper the {@link DynamoDBMapper} used to interact with the comment_table table
     */
    @Inject
    public CommentDao(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    /**
     * Saves a comment associated with a book.
     *
     * @param asin the asin of the book
     * @param commentText the text of the comment to save
     */
    public void saveCommentForBook(String asin, String commentText) {
        //Create & save new comment
        Comment comment = new Comment();
        comment.setAsin(asin);
        comment.setCommentText(commentText);

        dynamoDBMapper.save(comment);
    }

    /**
     * Retrieves the comments associated with a book.
     *
     * @param asin the asin of the book
     * @return a list of comments associated with the book
     */
    public List<Comment> getCommentsForBook(String asin) {
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":val", new AttributeValue().withS(asin));

        DynamoDBQueryExpression<Comment> queryExpression = new DynamoDBQueryExpression<Comment>()
                .withKeyConditionExpression("asin = :val")
                .withExpressionAttributeValues(valueMap);

        PaginatedQueryList<Comment> result = dynamoDBMapper.query(Comment.class, queryExpression);
        return new ArrayList<>(result);
    }
}

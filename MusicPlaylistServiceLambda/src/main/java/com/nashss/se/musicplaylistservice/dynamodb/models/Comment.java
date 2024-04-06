package com.nashss.se.musicplaylistservice.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

/**
 * Represents a Comment in the Comments table.
 */
@DynamoDBTable(tableName = "Comments")
public class Comment {

    private String commentId;
    private String asin;
    private String customerId;
    private Integer percentComplete;
    private String commentText;

    @DynamoDBHashKey(attributeName = "commentId")
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "CustomerIdAsinIndex", attributeName = "asin")
    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "CustomerIdAsinIndex", attributeName = "customerId")
    public String getCustomerId() {
        return customerId;
    }

    /**
     * setter for the customerId for a comment.
     */
    public void setCustomerId() {
        this.customerId = customerId;
    }

    @DynamoDBAttribute(attributeName = "percentComplete")
    public Integer getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(Integer percentComplete) {
        this.percentComplete = percentComplete;
    }

    @DynamoDBAttribute(attributeName = "commentText")
    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Comment comment = (Comment) o;
        return Objects.equals(commentId, comment.commentId) && Objects.equals(asin, comment.asin) &&
                Objects.equals(customerId, comment.customerId) &&
                Objects.equals(percentComplete, comment.percentComplete) &&
                Objects.equals(commentText, comment.commentText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, asin, customerId, percentComplete, commentText);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", asin='" + asin + '\'' +
                ", customerId='" + customerId + '\'' +
                ", percentComplete=" + percentComplete +
                ", commentText='" + commentText + '\'' +
                '}';
    }
}



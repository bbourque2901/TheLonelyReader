package com.nashss.se.musicplaylistservice.dynamodb.models;
import com.nashss.se.musicplaylistservice.converters.BookConverter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

import java.util.*;

@DynamoDBTable(tableName = "Booklists")
public class Booklist {
    private String id;
    private String name;
    private String customerId;
    private Integer bookCount;
    private Set<String> tags;
    private List<Book> books;
    private Map<Book, List<Comment>> bookComments;

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @DynamoDBAttribute(attributeName = "booklistName")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @DynamoDBAttribute(attributeName = "customerId")
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    @DynamoDBAttribute(attributeName = "bookCount")
    public Integer getBookCount() {
        return bookCount;
    }
    public void setBookCount(Integer bookCount) {
        this.bookCount = bookCount;
    }

    /**
     * getter for the tags for a booklist.
     * @return tags
     */
    @DynamoDBAttribute(attributeName = "tags")
    public Set<String> getTags() {
        if (null == tags) {
            return null;
        }
        return new HashSet<>(tags);
    }

    /**
     * setter for the tags for a booklist.
     * @param tags set of tags passed in
     */
    public void setTags(Set<String> tags) {
        if (null == tags) {
            this.tags = null;
        } else {
            this.tags = new HashSet<>(tags);
        }
    }
    @DynamoDBAttribute(attributeName = "bookList")
    @DynamoDBTypeConverted(converter = BookConverter.class)
    public List<Book> getBooks() {
        return books;
    }
    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Map<Book, List<Comment>> getBookComments() {
        return bookComments;
    }

    public void setBookComments(Map<Book, List<Comment>> bookComments) {
        this.bookComments = bookComments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Booklist bookList = (Booklist) o;
        return Objects.equals(id, bookList.id) &&
                Objects.equals(name, bookList.name) &&
                Objects.equals(customerId, bookList.customerId) &&
                Objects.equals(bookCount, bookList.bookCount) &&
                Objects.equals(tags, bookList.tags) &&
                Objects.equals(books, bookList.books) &&
                Objects.equals(bookComments, bookList.bookComments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, customerId, bookCount, tags, books, bookComments);
    }
}

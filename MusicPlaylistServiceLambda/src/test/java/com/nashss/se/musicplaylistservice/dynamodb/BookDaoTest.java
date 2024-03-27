package com.nashss.se.musicplaylistservice.dynamodb;

import com.nashss.se.musicplaylistservice.dynamodb.models.Book;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Captor;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

public class BookDaoTest {
    @Mock
    private DynamoDBMapper dynamoDBMapper;
    @Mock
    private PaginatedScanList<Book> pagScanList;
    @Captor
    ArgumentCaptor<DynamoDBScanExpression> scanExpCaptor;

    private BookDao bookDao;

    @BeforeEach
    public void setup() {
        initMocks(this);
        bookDao = new BookDao(dynamoDBMapper);
    }

    @Test
    public void searchBooks_withValidCriteria_returnsAListOfBooks() {
        // GIVEN
        String criteria = "not null";
        String[] criteriaArray = {criteria};
        when(dynamoDBMapper.scan(eq(Book.class),
                any(DynamoDBScanExpression.class))).thenReturn(pagScanList);

        // WHEN
        List<Book> results = bookDao.searchBooks(criteriaArray);

        // THEN
        verify(dynamoDBMapper).scan(eq(Book.class), scanExpCaptor.capture());
        assertNotNull(results);
        assertTrue(!results.isEmpty());
    }

    @Test
    public void searchBooks_withNullCriteria_returnsEmptyListOfBooks() {
        // GIVEN
        String criteria = null;
        String[] criteriaArray = {criteria};

        when(dynamoDBMapper.scan(eq(Book.class),
                any(DynamoDBScanExpression.class))).thenReturn(pagScanList);

        // WHEN
        List<Book> results = bookDao.searchBooks(criteriaArray);

        // THEN
        verify(dynamoDBMapper).scan(eq(Book.class), scanExpCaptor.capture());
        assertNotNull(results);
        assertTrue(results.size() == 0);
    }

}

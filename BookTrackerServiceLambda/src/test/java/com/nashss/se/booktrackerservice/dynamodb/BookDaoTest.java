package com.nashss.se.booktrackerservice.dynamodb;


import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.nashss.se.booktrackerservice.dynamodb.models.Booklist;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.initMocks;

import com.nashss.se.booktrackerservice.dynamodb.models.Book;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;


import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Captor;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class BookDaoTest {
    @Mock
    private DynamoDBMapper dynamoDBMapper;
    @Mock
    private PaginatedScanList<Book> pagScanList;
    @Mock
    private ScanResultPage<Book> scanResultPage;
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

    @Test
    public void getCurrentlyReading_withValidCriteria_returnsAListOfBooks() {
        // GIVEN
        boolean currentlyReading = true;
        Book book = new Book();
        book.setAsin("1234");
        List<Book> result = new ArrayList<>();
        result.add(book);
        when(dynamoDBMapper.scanPage(eq(Book.class),
                any(DynamoDBScanExpression.class))).thenReturn(scanResultPage);
        when(scanResultPage.getResults()).thenReturn(result);

        // WHEN
        Booklist results = bookDao.getCurrentlyReading(currentlyReading);

        // THEN
        verify(dynamoDBMapper).scanPage(eq(Book.class), scanExpCaptor.capture());
        assertNotNull(results);
    }

}

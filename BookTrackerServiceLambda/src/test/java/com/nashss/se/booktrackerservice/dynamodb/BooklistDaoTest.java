package com.nashss.se.booktrackerservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.nashss.se.booktrackerservice.dynamodb.models.Booklist;
import com.nashss.se.booktrackerservice.exceptions.BooklistNotFoundException;
import com.nashss.se.booktrackerservice.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BooklistDaoTest {
    @Mock
    private DynamoDBMapper dynamoDBMapper;
    @Mock
    private PaginatedScanList<Booklist> pagScanList;
    @Captor
    ArgumentCaptor<DynamoDBScanExpression> scanExpCaptor;

    private BooklistDao booklistDao;

    @BeforeEach
    public void setup() {
        initMocks(this);
        booklistDao = new BooklistDao(dynamoDBMapper);
    }

    @Test
    public void getBooklist_withBooklistId_callsMapperWithPartitionKey() {
        // GIVEN
        String testId = "testId";
        when(dynamoDBMapper.load(Booklist.class, testId)).thenReturn(new Booklist());

        // WHEN
        Booklist Booklist = booklistDao.getBooklist(testId);

        // THEN
        assertNotNull(Booklist);
        verify(dynamoDBMapper).load(Booklist.class, testId);
    }

    @Test
    public void getBooklist_booklistIdNotFound_throwsBooklistNotFoundException() {
        // GIVEN
        String nonExistentId = "NotReal";
        when(dynamoDBMapper.load(Booklist.class, nonExistentId)).thenReturn(null);

        // WHEN + THEN
        assertThrows(BooklistNotFoundException.class, () -> booklistDao.getBooklist(nonExistentId));
    }

    @Test
    public void saveBooklist_callsMapperWithBooklist() {
        // GIVEN
        Booklist booklist = new Booklist();

        // WHEN
        Booklist result = booklistDao.saveBooklist(booklist);

        // THEN
        verify(dynamoDBMapper).save(booklist);
        assertEquals(booklist, result);
    }

    @Test
    public void searchBooklist_withValidCriteria_returnsAListOfBooklists() {
        // GIVEN
        String criteria = "not null";
        String[] criteriaArray = {criteria};
        when(dynamoDBMapper.scan(eq(Booklist.class),
                any(DynamoDBScanExpression.class))).thenReturn(pagScanList);

        // WHEN
        List<Booklist> results = booklistDao.searchBooklists(criteriaArray);

        // THEN
        verify(dynamoDBMapper).scan(eq(Booklist.class), scanExpCaptor.capture());
        assertNotNull(results);
        assertTrue(!results.isEmpty());
    }

    @Test
    public void searchBooklist_withNullCriteria_returnsEmptyListOfBooklist() {
        // GIVEN
        String criteria = null;
        String[] criteriaArray = {criteria};

        when(dynamoDBMapper.scan(eq(Booklist.class),
                any(DynamoDBScanExpression.class))).thenReturn(pagScanList);

        // WHEN
        List<Booklist> results = booklistDao.searchBooklists(criteriaArray);

        // THEN
        verify(dynamoDBMapper).scan(eq(Booklist.class), scanExpCaptor.capture());
        assertNotNull(results);
        assertTrue(results.size() == 0);
    }

    @Test
    public void getAllBooklistsForUser_withUserId_returnsListOfBooklists() {
        // GIVEN
        String testId = "testId";
        when(dynamoDBMapper.scan(eq(Booklist.class), any(DynamoDBScanExpression.class))).thenReturn(pagScanList);

        // WHEN
        List<Booklist> results = booklistDao.getAllBooklistsForUser(testId);

        // THEN
        assertNotNull(results);
        verify(dynamoDBMapper).scan(eq(Booklist.class), scanExpCaptor.capture());
        assertTrue(!results.isEmpty());
    }

    @Test
    public void getAllBooklistsForUser_withNullId_throwsUserNotFoundException() {
        // GIVEN
        String testId = null;
        when(dynamoDBMapper.scan(eq(Booklist.class), any(DynamoDBScanExpression.class))).thenReturn(pagScanList);


        // WHEN + THEN
        assertThrows(UserNotFoundException.class, () -> booklistDao.getAllBooklistsForUser(testId));
    }
}

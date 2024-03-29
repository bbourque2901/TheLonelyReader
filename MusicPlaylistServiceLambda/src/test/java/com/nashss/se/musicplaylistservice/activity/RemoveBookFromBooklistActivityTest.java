package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.RemoveBookFromBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.RemoveBookFromBooklistResult;
import com.nashss.se.musicplaylistservice.dynamodb.BookDao;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Book;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.exceptions.BookNotFoundException;
import com.nashss.se.musicplaylistservice.exceptions.BooklistNotFoundException;
import com.nashss.se.musicplaylistservice.test.helper.BookTestHelper;
import com.nashss.se.musicplaylistservice.test.helper.BooklistTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RemoveBookFromBooklistActivityTest {
    @Mock
    private BooklistDao booklistDao;
    @Mock
    private BookDao bookDao;
    private RemoveBookFromBooklistActivity removeBookFromBooklistActivity;

    @BeforeEach
    void setUp() {
        initMocks(this);
        this.removeBookFromBooklistActivity = new RemoveBookFromBooklistActivity(booklistDao, bookDao);
    }

    @Test
    void handleRequest_validRequest_removesFromBooklist() {
        //GIVEN
        Booklist originalBooklist = BooklistTestHelper.generateBooklist();
        String booklistId = originalBooklist.getId();
        String customerId = originalBooklist.getCustomerId();

        Book bookToRemove = BookTestHelper.generateBook(2);
        String removeAsin = bookToRemove.getAsin();

        originalBooklist.getBooks().add(bookToRemove);

        when(booklistDao.getBooklist(booklistId)).thenReturn(originalBooklist);
        when(booklistDao.saveBooklist(originalBooklist)).thenReturn(originalBooklist);
        when(bookDao.getBook(removeAsin)).thenReturn(bookToRemove);

        RemoveBookFromBooklistRequest request = RemoveBookFromBooklistRequest.builder()
                .withAsin(removeAsin)
                .withId(booklistId)
                .withCustomerId(customerId)
                .build();

        //WHEN
        RemoveBookFromBooklistResult result = removeBookFromBooklistActivity.handleRequest(request);

        //THEN;
        assertEquals(1, result.getBooklist().size());
        assertFalse(originalBooklist.getBooks().contains(bookToRemove));
    }
    @Test
    public void handleRequest_noMatchingBooklistId_throwsBooklistNotFoundException() {
        // GIVEN
        String booklistId = "missing id";
        RemoveBookFromBooklistRequest request = RemoveBookFromBooklistRequest.builder()
                .withId(booklistId)
                .withAsin("asin")
                .withCustomerId("doesn't matter")
                .build();
        when(booklistDao.getBooklist(booklistId)).thenThrow(new BooklistNotFoundException());

        // WHEN + THEN
        assertThrows(BooklistNotFoundException.class, () -> removeBookFromBooklistActivity.handleRequest(request));
    }
    @Test
    public void handleRequest_noMatchingBook_throwsBookNotFoundException() {
        // GIVEN
        Booklist booklist = BooklistTestHelper.generateBooklist();

        String booklistId = booklist.getId();
        String customerId = booklist.getCustomerId();
        String asin = "nonexistent asin";
        RemoveBookFromBooklistRequest request = RemoveBookFromBooklistRequest.builder()
                .withId(booklistId)
                .withAsin(asin)
                .withCustomerId(customerId)
                .build();

        // WHEN
        when(booklistDao.getBooklist(booklistId)).thenReturn(booklist);
        when(bookDao.getBook(asin)).thenThrow(new BookNotFoundException());

        // THEN
        assertThrows(BookNotFoundException.class, () -> removeBookFromBooklistActivity.handleRequest(request));
    }
}

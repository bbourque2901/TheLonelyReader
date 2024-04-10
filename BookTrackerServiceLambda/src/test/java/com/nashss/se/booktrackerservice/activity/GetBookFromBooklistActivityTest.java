package com.nashss.se.booktrackerservice.activity;

import com.nashss.se.booktrackerservice.activity.requests.GetBookFromBooklistRequest;
import com.nashss.se.booktrackerservice.activity.results.GetBookFromBooklistResult;
import com.nashss.se.booktrackerservice.dynamodb.BooklistDao;
import com.nashss.se.booktrackerservice.dynamodb.models.Booklist;
import com.nashss.se.booktrackerservice.exceptions.BookNotFoundException;
import com.nashss.se.booktrackerservice.test.helper.BooklistTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetBookFromBooklistActivityTest {

    @Mock
    private BooklistDao booklistDao;
    private GetBookFromBooklistActivity getBookFromBooklistActivity;

    @BeforeEach
    public void setup() {
        initMocks(this);
        getBookFromBooklistActivity = new GetBookFromBooklistActivity(booklistDao);
    }

    @Test
    public void handleRequest_savedBooklistFoundAndBookFound_returnsBookModelInResult() {
        // GIVEN
        Booklist expBooklist = BooklistTestHelper.generateBooklistWithNBooks(3);
        String expBookAsin = expBooklist.getBooks().get(0).getAsin();
        String expBooklistId = expBooklist.getId();
        when(booklistDao.getBooklist(expBooklistId)).thenReturn(expBooklist);

        GetBookFromBooklistRequest request = GetBookFromBooklistRequest.builder()
                .withBooklistId(expBooklistId)
                .withBookAsin(expBookAsin)
                .build();

        // WHEN
        GetBookFromBooklistResult result = getBookFromBooklistActivity.handleRequest(request);

        // THEN
        assertEquals(result.getBook().getTitle(), expBooklist.getBooks().get(0).getTitle());
        assertEquals(result.getBook().getAsin(), expBooklist.getBooks().get(0).getAsin());
        assertEquals(result.getBook().getAuthor(), expBooklist.getBooks().get(0).getAuthor());
        assertEquals(result.getBook().getGenre(), expBooklist.getBooks().get(0).getGenre());
    }

    @Test
    public void handleRequest_savedBooklistFoundAndBookNotFound_throwsBookNotFoundException() {
        // GIVEN
        Booklist expBooklist = BooklistTestHelper.generateBooklistWithNBooks(3);
        String nonexistAsin = "test";
        String expBooklistId = expBooklist.getId();
        when(booklistDao.getBooklist(expBooklistId)).thenReturn(expBooklist);

        GetBookFromBooklistRequest request = GetBookFromBooklistRequest.builder()
                .withBooklistId(expBooklistId)
                .withBookAsin(nonexistAsin)
                .build();

        // WHEN & THEN
        assertThrows(BookNotFoundException.class, ()-> getBookFromBooklistActivity.handleRequest(request));
    }
}

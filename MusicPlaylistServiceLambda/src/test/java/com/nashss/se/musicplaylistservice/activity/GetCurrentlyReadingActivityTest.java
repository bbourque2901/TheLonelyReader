package com.nashss.se.musicplaylistservice.activity;

import com.google.common.collect.Sets;
import com.nashss.se.musicplaylistservice.activity.requests.GetBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.requests.GetCurrentlyReadingRequest;
import com.nashss.se.musicplaylistservice.activity.results.GetBooklistResult;
import com.nashss.se.musicplaylistservice.activity.results.GetCurrentlyReadingResult;
import com.nashss.se.musicplaylistservice.dynamodb.BookDao;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Book;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetCurrentlyReadingActivityTest {
    @Mock
    private BooklistDao booklistDao;
    @Mock
    private BookDao bookDao;

    private GetCurrentlyReadingActivity getCurrentlyReadingActivity;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        getCurrentlyReadingActivity = new GetCurrentlyReadingActivity(bookDao, booklistDao);
    }

    @Test
    public void handleRequest_savedBooklistFound_returnsBooklistWithOnlyCurrentlyReadingAsins() {
        // GIVEN
        String expectedId = "expectedId";
        String expectedName = "expectedName";
        String expectedCustomerId = "expectedCustomerId";
        int expectedBookCount = 0;
        List<String> expectedTags = List.of("tag");
        Book book1 = new Book();
        book1.setAsin("111");
        Book book2 = new Book();
        book2.setAsin("222");
        Book book3 = new Book();
        book3.setAsin("333");
        Book book4 = new Book();
        book4.setAsin("current1");
        book4.setCurrentlyReading(true);
        Book book5 = new Book();
        book5.setAsin("current2");
        book5.setCurrentlyReading(true);
        Book book6 = new Book();
        book6.setAsin("666");
        List<Book> booksGiven = List.of(book1, book2, book3, book4, book5, book6);
        for (Book book : booksGiven) {
            book.setTitle("title");
            book.setAuthor("author");
        }

        Booklist booklist = new Booklist();
        booklist.setId(expectedId);
        booklist.setName(expectedName);
        booklist.setCustomerId(expectedCustomerId);
        booklist.setBookCount(expectedBookCount);
        booklist.setTags(Sets.newHashSet(expectedTags));
        booklist.setBooks(booksGiven);

        Booklist currentlyReading = new Booklist();
        currentlyReading.setBooks(List.of(book4, book5));

        when(booklistDao.getBooklist(expectedId)).thenReturn(booklist);
        when(bookDao.getCurrentlyReading(true)).thenReturn(currentlyReading);

        GetCurrentlyReadingRequest request = GetCurrentlyReadingRequest.builder()
                .withCurrentlyReading(true)
                .withId(expectedId)
                .build();

        // WHEN
        GetCurrentlyReadingResult result = getCurrentlyReadingActivity.handleRequest(request);

        // THEN
        assertEquals(expectedId, result.getBooklist().getId());
        assertEquals(expectedName, result.getBooklist().getName());
        assertEquals(expectedCustomerId, result.getBooklist().getCustomerId());
        assertEquals(expectedBookCount, result.getBooklist().getBookCount());
        assertEquals(expectedTags, result.getBooklist().getTags());
        assertEquals(List.of(book4, book5), result.getBooklist().getBooks());
    }
}

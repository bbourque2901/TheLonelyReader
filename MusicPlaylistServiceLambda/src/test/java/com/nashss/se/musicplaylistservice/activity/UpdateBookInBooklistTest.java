package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.UpdateBookInBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.UpdateBookInBooklistResult;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.BookDao;
import com.nashss.se.musicplaylistservice.dynamodb.CommentDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Book;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class UpdateBookInBooklistTest {

    @Mock
    private BooklistDao booklistDao;
    @Mock
    private BookDao bookDao;
    @Mock
    private CommentDao commentDao;
    @Mock
    private MetricsPublisher metricsPublisher;
    private UpdateBookInBooklistActivity updateBookInBooklistActivity;
    @Captor
    ArgumentCaptor<Booklist> booklistCaptor;
    private Booklist booklist1;
    private Book book1;

    @BeforeEach
    public void setup() {
        openMocks(this);
        updateBookInBooklistActivity = new UpdateBookInBooklistActivity(booklistDao, bookDao, metricsPublisher, commentDao);
        // GIVEN
        String expectedId = "expectedId";
        String expectedName = "expectedName";
        String expectedCustomerId = "expectedCustomerId";
        int expectedBookCount = 0;
        List<String> expectedTags = List.of("tag");
        Book book1 = new Book();
        book1.setAsin("111");
        book1.setCurrentlyReading(false);
        book1.setPercentComplete(null);
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
        Booklist booklist1 = new Booklist();
        booklist1.setName(expectedName);
        booklist1.setBooks(booksGiven);
        booklist1.setId(expectedId);
        booklist1.setCustomerId(expectedCustomerId);
        this.booklist1 = booklist1;
        this.book1 = booklist1.getBooks().get(0);
    }

    @Test
    public void handleRequest_updateCurrentlyReadingOnly_onlyUpdatesAttribute() {
        //Given
        UpdateBookInBooklistRequest request = UpdateBookInBooklistRequest.builder()
                .withAsin("111")
                .withCustomerId("expectedCustomerId")
                .withCurrentlyReading(true)
                .build();
        when(bookDao.getBook("111")).thenReturn(book1);
        when(booklistDao.getAllBooklistsForUser("expectedCustomerId")).thenReturn(List.of(booklist1));

        //when
        UpdateBookInBooklistResult result = updateBookInBooklistActivity.handleRequest(request);

        ArgumentCaptor<Booklist> captor = ArgumentCaptor.forClass(Booklist.class);
        verify(booklistDao).saveBooklist(captor.capture());
        Booklist savedBooklist = captor.getValue();
        Book savedBook = savedBooklist.getBooks().get(5);
        //then
        assertEquals("111", result.getBookModel().getAsin());
        assertTrue(result.getBookModel().isCurrentlyReading());
        assertEquals(0, result.getBookModel().getPercentComplete());

        assertEquals("111", savedBook.getAsin());
        assertTrue(savedBook.isCurrentlyReading());
        assertEquals(0, savedBook.getPercentComplete());
    }

    @Test
    public void handleRequest_updatePercentOnly_onlyUpdatesAttribute() {
        //Given
        UpdateBookInBooklistRequest request = UpdateBookInBooklistRequest.builder()
                .withAsin("111")
                .withCustomerId("expectedCustomerId")
                .withPercentComplete(25)
                .build();
        when(bookDao.getBook("111")).thenReturn(book1);
        when(booklistDao.getAllBooklistsForUser("expectedCustomerId")).thenReturn(List.of(booklist1));
        //when
        UpdateBookInBooklistResult result = updateBookInBooklistActivity.handleRequest(request);

        ArgumentCaptor<Booklist> captor = ArgumentCaptor.forClass(Booklist.class);
        verify(booklistDao).saveBooklist(captor.capture());
        Booklist savedBooklist = captor.getValue();
        Book savedBook = savedBooklist.getBooks().get(5);
        //then
        assertEquals("111", result.getBookModel().getAsin());
        assertFalse(result.getBookModel().isCurrentlyReading());
        assertEquals(25, result.getBookModel().getPercentComplete());

        assertEquals("111", savedBook.getAsin());
        assertFalse(savedBook.isCurrentlyReading());
        assertEquals(25, savedBook.getPercentComplete());
    }

    @Test
    public void handleRequest_updatePercentAndCurrentlyReading_updatesBoth() {
        //Given
        UpdateBookInBooklistRequest request = UpdateBookInBooklistRequest.builder()
                .withAsin("111")
                .withCustomerId("expectedCustomerId")
                .withPercentComplete(25)
                .withCurrentlyReading(true)
                .build();
        when(bookDao.getBook("111")).thenReturn(book1);
        when(booklistDao.getAllBooklistsForUser("expectedCustomerId")).thenReturn(List.of(booklist1));
        //when
        UpdateBookInBooklistResult result = updateBookInBooklistActivity.handleRequest(request);

        ArgumentCaptor<Booklist> captor = ArgumentCaptor.forClass(Booklist.class);
        verify(booklistDao).saveBooklist(captor.capture());
        Booklist savedBooklist = captor.getValue();
        Book savedBook = savedBooklist.getBooks().get(5);
        //then
        assertEquals("111", result.getBookModel().getAsin());
        assertTrue(result.getBookModel().isCurrentlyReading());
        assertEquals(25, result.getBookModel().getPercentComplete());

        assertEquals("111", savedBook.getAsin());
        assertTrue(savedBook.isCurrentlyReading());
        assertEquals(25, savedBook.getPercentComplete());
    }
    @Test
    public void handleRequest_updateOnlyRating_updatesOnlyOneAttribute() {
        //Given
        UpdateBookInBooklistRequest request = UpdateBookInBooklistRequest.builder()
                .withAsin("111")
                .withCustomerId("expectedCustomerId")
                .withRating(7)
                .build();
        when(bookDao.getBook("111")).thenReturn(book1);
        when(booklistDao.getAllBooklistsForUser("expectedCustomerId")).thenReturn(List.of(booklist1));
        //when
        UpdateBookInBooklistResult result = updateBookInBooklistActivity.handleRequest(request);

        ArgumentCaptor<Booklist> captor = ArgumentCaptor.forClass(Booklist.class);
        verify(booklistDao).saveBooklist(captor.capture());
        Booklist savedBooklist = captor.getValue();
        Book savedBook = savedBooklist.getBooks().get(5);
        //then
        assertEquals("111", result.getBookModel().getAsin());
        assertFalse(result.getBookModel().isCurrentlyReading());
        assertEquals(7, result.getBookModel().getRating());

        assertEquals("111", savedBook.getAsin());
        assertFalse(savedBook.isCurrentlyReading());
        assertEquals(7, savedBook.getRating());
    }
    @Test
    public void handleRequest_updateAllThreeAttributes_updatesInBothBooklists() {
        //Given
        Booklist booklist2 = new Booklist();
        booklist2.setBooks(List.of(book1));
        booklist2.setCustomerId("expectedCustomerId");
        booklist2.setId("SecondBookListId");

        UpdateBookInBooklistRequest request = UpdateBookInBooklistRequest.builder()
                .withAsin("111")
                .withCustomerId("expectedCustomerId")
                .withRating(7)
                .withCurrentlyReading(true)
                .withPercentComplete(50)
                .build();

        when(bookDao.getBook("111")).thenReturn(book1);
        when(booklistDao.getAllBooklistsForUser("expectedCustomerId")).thenReturn(List.of(booklist1, booklist2));

        //when
        UpdateBookInBooklistResult result = updateBookInBooklistActivity.handleRequest(request);

        ArgumentCaptor<Booklist> captor = ArgumentCaptor.forClass(Booklist.class);
        verify(booklistDao, times(2)).saveBooklist(captor.capture());
        List<Booklist> capturedLists = captor.getAllValues();
        Booklist savedBooklist = capturedLists.get(0);
        Book savedBook = savedBooklist.getBooks().get(5);
        Booklist savedBooklist2 = capturedLists.get(1);
        Book savedBook2 = savedBooklist2.getBooks().get(0);

        //then
        assertEquals("111", result.getBookModel().getAsin());
        assertTrue(result.getBookModel().isCurrentlyReading());
        assertEquals(50, result.getBookModel().getPercentComplete());
        assertEquals(7, result.getBookModel().getRating());

        assertEquals("111", savedBook.getAsin());
        assertTrue(savedBook.isCurrentlyReading());
        assertEquals(7, savedBook.getRating());
        assertEquals(50, savedBook.getPercentComplete());

        assertEquals("SecondBookListId", savedBooklist2.getId());
        assertEquals("111", savedBook2.getAsin());
        assertTrue(savedBook2.isCurrentlyReading());
        assertEquals(7, savedBook2.getRating());
        assertEquals(50, savedBook2.getPercentComplete());
    }
}

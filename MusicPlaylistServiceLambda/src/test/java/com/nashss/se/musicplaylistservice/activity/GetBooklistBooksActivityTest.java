package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.GetBooklistBooksRequest;
import com.nashss.se.musicplaylistservice.activity.results.GetBooklistBooksResult;
import com.nashss.se.musicplaylistservice.converters.ModelConverterCarbon;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Book;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.exceptions.BooklistNotFoundException;
import com.nashss.se.musicplaylistservice.exceptions.InvalidAttributeValueException;
import com.nashss.se.musicplaylistservice.models.BookModel;
import com.nashss.se.musicplaylistservice.models.SongOrder;
import com.nashss.se.musicplaylistservice.test.helper.BookTestHelper;
import com.nashss.se.musicplaylistservice.test.helper.BooklistTestHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetBooklistBooksActivityTest {

    @Mock
    private BooklistDao booklistDao;

    private GetBooklistBooksActivity getBooklistBooksActivity;

    @BeforeEach
    void setup() {
        openMocks(this);
        getBooklistBooksActivity = new GetBooklistBooksActivity(booklistDao);
    }

    @Test
    void handleRequest_booklistExistsWithBooks_returnsBooksInBooklist() {
        // GIVEN
        Booklist booklist = BooklistTestHelper.generateBooklistWithNBooks(3);
        String booklistId = booklist.getId();
        GetBooklistBooksRequest request = GetBooklistBooksRequest.builder()
                .withId(booklistId)
                .build();
        when(booklistDao.getBooklist(booklistId)).thenReturn(booklist);

        // WHEN
        GetBooklistBooksResult result = getBooklistBooksActivity.handleRequest(request);

        // THEN
        BookTestHelper.assertBooksEqualBookModels(booklist.getBooks(), result.getBooks());
    }

    @Test
    void handleRequest_withDefaultBookOrder_returnsDefaultOrderedBooklistBooks() {
        // GIVEN
        Booklist booklist = BooklistTestHelper.generateBooklistWithNBooks(10);
        String booklistId = booklist.getId();

        GetBooklistBooksRequest request = GetBooklistBooksRequest.builder()
                .withId(booklistId)
                .withOrder(SongOrder.DEFAULT)
                .build();
        when(booklistDao.getBooklist(booklistId)).thenReturn(booklist);

        // WHEN
        GetBooklistBooksResult result = getBooklistBooksActivity.handleRequest(request);

        // THEN
        BookTestHelper.assertBooksEqualBookModels(booklist.getBooks(), result.getBooks());
    }

    @Test
    void handleRequest_withReversedBookOrder_returnsReversedBooklistBooks() {
        // GIVEN
        Booklist booklist = BooklistTestHelper.generateBooklistWithNBooks(9);
        String booklistId = booklist.getId();
        List<Book> reversedBooks = new LinkedList<>(booklist.getBooks());
        Collections.reverse(reversedBooks);

        GetBooklistBooksRequest request = GetBooklistBooksRequest.builder()
                .withId(booklistId)
                .withOrder(SongOrder.REVERSED)
                .build();
        when(booklistDao.getBooklist(booklistId)).thenReturn(booklist);

        // WHEN
        GetBooklistBooksResult result = getBooklistBooksActivity.handleRequest(request);

        // THEN
        BookTestHelper.assertBooksEqualBookModels(reversedBooks, result.getBooks());
    }

    @Test
    void handleRequest_withShuffledBookOrder_returnsBooklistBooksInAnyOrder() {
        // GIVEN
        Booklist booklist = BooklistTestHelper.generateBooklistWithNBooks(8);
        String booklistId = booklist.getId();

        List<BookModel> bookModels = new ModelConverterCarbon().toBookModelList(booklist.getBooks());

        GetBooklistBooksRequest request = GetBooklistBooksRequest.builder()
                .withId(booklistId)
                .withOrder(SongOrder.SHUFFLED)
                .build();
        when(booklistDao.getBooklist(booklistId)).thenReturn(booklist);

        // WHEN
        GetBooklistBooksResult result = getBooklistBooksActivity.handleRequest(request);

        // THEN
        assertEquals(booklist.getBooks().size(),
                result.getBooks().size(),
                String.format("Expected books (%s) and book models (%s) to be the same length",
                        booklist.getBooks(),
                        result.getBooks()));
        assertTrue(
                bookModels.containsAll(result.getBooks()),
                String.format("book list (%s) and book models (%s) are the same length, but don't contain the same " +
                                "entries. Expected book models: %s. Returned book models: %s",
                        booklist.getBooks(),
                        result.getBooks(),
                        bookModels,
                        result.getBooks()));
    }

    @Test
    public void handleRequest_noMatchingBooklistId_throwsBooklistNotFoundException() {
        // GIVEN
        String id = "missingID";
        GetBooklistBooksRequest request = GetBooklistBooksRequest.builder()
                .withId(id)
                .build();

        // WHEN
        when(booklistDao.getBooklist(id)).thenThrow(new BooklistNotFoundException());

        // WHEN + THEN
        assertThrows(BooklistNotFoundException.class, () -> getBooklistBooksActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_withInvalidBookOrder_throwsException() {
        // GIVEN
        Booklist booklist = BooklistTestHelper.generateBooklist();
        String id = booklist.getId();
        GetBooklistBooksRequest request = GetBooklistBooksRequest.builder()
                .withId(id)
                .withOrder("NOT A VALID ORDER")
                .build();

        // WHEN + THEN
        assertThrows(InvalidAttributeValueException.class, () -> getBooklistBooksActivity.handleRequest(request));
    }
}

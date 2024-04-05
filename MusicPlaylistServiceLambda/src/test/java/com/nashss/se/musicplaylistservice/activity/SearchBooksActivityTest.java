package com.nashss.se.musicplaylistservice.activity;


import com.nashss.se.musicplaylistservice.activity.requests.SearchBooksRequest;
import com.nashss.se.musicplaylistservice.activity.results.SearchBooksResult;
import com.nashss.se.musicplaylistservice.dynamodb.BookDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Book;
import com.nashss.se.musicplaylistservice.models.BookModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SearchBooksActivityTest {
    @Mock
    private BookDao bookDao;
    private SearchBooksActivity searchBooksActivity;

    @BeforeEach
    public void setup() {
        initMocks(this);
        searchBooksActivity = new SearchBooksActivity(bookDao);
    }

    @Test
    public void handleRequest_whenBooksMatchSearch_returnsBookModelInResult() {
        // GIVEN
        String criteria = "good";
        String[] criteriaArray = {criteria};

        List<Book> expected = List.of(
                newBook("123", "goodTitle", "whatser name", "thriller"),
                newBook("456", "goodTitleToo", "whos he", "romance"));

        when(bookDao.searchBooks(criteriaArray)).thenReturn(expected);

        SearchBooksRequest request = SearchBooksRequest.builder()
                .withCriteria(criteria)
                .build();

        // WHEN
        SearchBooksResult result = searchBooksActivity.handleRequest(request);

        // THEN
        List<BookModel> resultBooks = result.getBooks();
        assertEquals(expected.size(), resultBooks.size());

        for (int i=0; i<expected.size(); i++) {
            assertEquals(expected.get(i).getAsin(), resultBooks.get(i).getAsin());
            assertEquals(expected.get(i).getTitle(), resultBooks.get(i).getTitle());
        }
    }

    @Test
    public void handleRequest_withNullCriteria_isIdenticalToEmptyCriteria() {
        // GIVEN
        String criteria = null;
        ArgumentCaptor<String[]> criteriaArray = ArgumentCaptor.forClass(String[].class);

        when(bookDao.searchBooks(criteriaArray.capture())).thenReturn(List.of());

        SearchBooksRequest request = SearchBooksRequest.builder()
                .withCriteria(criteria)
                .build();

        // WHEN
        SearchBooksResult result = searchBooksActivity.handleRequest(request);

        // THEN
        assertEquals(0, criteriaArray.getValue().length, "Criteria Array should be empty");
    }

    private static Book newBook(String asin, String title, String author, String genre) {
        Book book = new Book();

        book.setAsin(asin);
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);

        // these can be any attributes for this test
        book.setRating(5);
        book.setComments(new ArrayList<>());
        book.setCurrentlyReading(true);
        book.setPercentComplete(50);

        return book;
    }
}

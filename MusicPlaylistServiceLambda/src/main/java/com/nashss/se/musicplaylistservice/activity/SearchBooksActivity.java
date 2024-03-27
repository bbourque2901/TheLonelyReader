package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.SearchBooksRequest;
import com.nashss.se.musicplaylistservice.activity.results.SearchBooksResult;
import com.nashss.se.musicplaylistservice.converters.ModelConverterCarbon;
import com.nashss.se.musicplaylistservice.dynamodb.BookDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Book;
import com.nashss.se.musicplaylistservice.models.BookModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.nashss.se.musicplaylistservice.utils.NullUtils.ifNull;

/**
 * Implementation of the SearchBooksActivity for the MusicPlaylistService's SearchBooks API.
 * <p>
 * This API allows the customer to search for books by title or asin.
 */
public class SearchBooksActivity {

    private final Logger log = LogManager.getLogger();
    private final BookDao bookDao;

    /**
     * Instantiates a new SearchBooksActivity object.
     *
     * @param bookDao BookDao to access the book table.
     */
    @Inject
    public SearchBooksActivity(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    /**
     * This method handles the incoming request by searching for books from the database.
     * <p>
     * It then returns the matching books, or an empty result list if none are found.
     *
     * @param searchBooksRequest request object containing the search criteria
     * @return searchBooksResult result object containing the books that match the
     * search criteria.
     */
    public SearchBooksResult handleRequest(SearchBooksRequest searchBooksRequest) {
        log.info("Recieved SearchBooksRequest {}", searchBooksRequest);

        String criteria = ifNull(searchBooksRequest.getCriteria(), "");
        String[] criteriaArray = criteria.isBlank() ? new String[0] : criteria.split("\\s");

        List<Book> results = bookDao.searchBooks(criteriaArray);
        List<BookModel> bookModels = new ArrayList<>();
        for (Book book : results) {
            bookModels.add(new ModelConverterCarbon().toBookModel(book));
        }

        return SearchBooksResult.builder()
                .withBooks(bookModels)
                .build();
    }
}

package com.nashss.se.booktrackerservice.activity;

import com.nashss.se.booktrackerservice.activity.requests.SearchBooksRequest;
import com.nashss.se.booktrackerservice.activity.results.SearchBooksResult;
import com.nashss.se.booktrackerservice.converters.ModelConverterCarbon;
import com.nashss.se.booktrackerservice.dynamodb.BookDao;
import com.nashss.se.booktrackerservice.dynamodb.models.Book;
import com.nashss.se.booktrackerservice.exceptions.GoogleBookAPISearchException;
import com.nashss.se.booktrackerservice.googlebookapi.Request;
import com.nashss.se.booktrackerservice.googlebookapi.helper.VolumeInfoHelper;
import com.nashss.se.booktrackerservice.models.BookModel;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.books.v1.model.Volume;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.nashss.se.booktrackerservice.utils.NullUtils.ifNull;

/**
 * Implementation of the SearchBooksActivity for the BookTrackerService's SearchBooks API.
 * <p>
 * This API allows the customer to search for books by title or asin.
 */
public class SearchBooksActivity {

    private final Logger log = LogManager.getLogger();
    private final BookDao bookDao;
    private Request googleBookApi;
    private VolumeInfoHelper helper;

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
        if (results == null || results.isEmpty()) {
            results = new ArrayList<>();
            try {
                JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
                googleBookApi = new Request();
                helper = new VolumeInfoHelper();
                List<Volume> volumes = googleBookApi.queryBooks(jsonFactory, searchBooksRequest.getCriteria());
                for (int i = 0; i < 5; i++) {
                    results.add(googleBookApi.deserializeVolumeToBook(googleBookApi.extractAttributes(volumes, i)));
                }
            } catch (Exception e) {
                throw new GoogleBookAPISearchException("Error with request to Google Book API");
            }
        }

        for (Book book : results) {
            bookModels.add(new ModelConverterCarbon().toBookModel(book));
        }

        return SearchBooksResult.builder()
                .withBooks(bookModels)
                .build();
    }
}

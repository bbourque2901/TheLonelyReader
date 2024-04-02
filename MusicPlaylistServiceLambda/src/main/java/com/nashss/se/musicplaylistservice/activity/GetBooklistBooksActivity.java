package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.GetBooklistBooksRequest;
import com.nashss.se.musicplaylistservice.activity.results.GetBooklistBooksResult;
import com.nashss.se.musicplaylistservice.converters.ModelConverterCarbon;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.exceptions.InvalidAttributeValueException;
import com.nashss.se.musicplaylistservice.models.BookModel;
import com.nashss.se.musicplaylistservice.models.BookOrder;
import com.nashss.se.musicplaylistservice.models.SongOrder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the GetBooklistBooksActivity for the MusicPlaylistService's GetBooklistBooks API.
 * <p>
 * This API allows the customer to get the list of books of a saved booklist.
 */
public class GetBooklistBooksActivity {
    
    private final Logger log = LogManager.getLogger();
    private final BooklistDao booklistDao;

    /**
     * Instantiates a new GetBooklistBooksActivity object.
     *
     * @param booklistDao BooklistDao to access the booklist table.
     */
    @Inject
    public GetBooklistBooksActivity(BooklistDao booklistDao) {
        this.booklistDao = booklistDao;
    }

    /**
     * This method handles the incoming request by retrieving the booklist from the database.
     * <p>
     * It then returns the booklist's book list.
     * <p>
     * If the booklist does not exist, this should throw a BooklistNotFoundException.
     *
     * @param getBooklistBooksRequest request object containing the booklist ID
     * @return getBooklistBooksResult result object containing the booklist's list of API defined {@link BookModel}s
     */
    public GetBooklistBooksResult handleRequest(final GetBooklistBooksRequest getBooklistBooksRequest) {
        log.info("Received GetPlaylistSongsRequest {}", getBooklistBooksRequest);

        String bookOrder = computeBookOrder(getBooklistBooksRequest.getOrder());

        Booklist booklist = booklistDao.getBooklist(getBooklistBooksRequest.getId());
        List<BookModel> bookModels = new ModelConverterCarbon().toBookModelList(booklist.getBooks());

        if (bookOrder.equals(SongOrder.REVERSED)) {
            Collections.reverse(bookModels);
        } else if (bookOrder.equals(SongOrder.SHUFFLED)) {
            Collections.shuffle(bookModels);
        }

        return GetBooklistBooksResult.builder()
                .withBooks(bookModels)
                .build();
    }

    /**
     * private helper method for computing the book order.
     */
    private String computeBookOrder(String bookOrder) {
        String computedBookOrder = bookOrder;

        if (null == bookOrder) {
            computedBookOrder = BookOrder.DEFAULT;
        } else if (!Arrays.asList(BookOrder.values()).contains(bookOrder)) {
            throw new InvalidAttributeValueException(String.format("Unrecognized sort order: '%s'", bookOrder));
        }

        return computedBookOrder;
    }
}

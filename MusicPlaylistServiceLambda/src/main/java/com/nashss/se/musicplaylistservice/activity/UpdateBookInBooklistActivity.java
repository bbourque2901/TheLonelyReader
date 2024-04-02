package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.UpdateBookInBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.UpdateBookInBooklistResult;
import com.nashss.se.musicplaylistservice.converters.ModelConverter;
import com.nashss.se.musicplaylistservice.converters.ModelConverterCarbon;
import com.nashss.se.musicplaylistservice.dynamodb.BookDao;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Book;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.metrics.MetricsPublisher;
import com.nashss.se.musicplaylistservice.models.BooklistModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class UpdateBookInBooklistActivity {
    /**
     * Implementation of the UpdateBookInBooklistActivity for the MusicPlaylistService's UpdateBooklist API.
     *
     * This API allows the customer to update their saved book's information. Like rating and percent complete.
     */
    private final Logger log = LogManager.getLogger();
    private final BooklistDao booklistDao;
    private final BookDao bookDao;
    private final MetricsPublisher metricsPublisher;

    /**
     * Instantiates a new UpdateBooklist object.
     *
     * @param booklistDao BooklistDao to access the booklist table.
     * @param bookDao BookDao to access the book table.
     * @param metricsPublisher MetricsPublisher to publish metrics.
     */
    @Inject
    public UpdateBookInBooklistActivity(BooklistDao booklistDao, BookDao bookDao, MetricsPublisher metricsPublisher) {
        this.booklistDao = booklistDao;
        this.bookDao = bookDao;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * This method handles the incoming request by retrieving the book, updating it,
     * and persisting it across booklists.
     * <p>
     * It then returns the updated book.
     * <p>
     * If the booklist does not exist, this should throw a BookNotFoundException.
     * <p>
     * If the provided booklist name or customer ID has invalid characters, throws an
     * InvalidAttributeValueException
     * <p>
     * If the request tries to update the customer ID,
     * this should throw an InvalidAttributeChangeException
     *
     * @param updateBookInBooklistRequest request object containing the playlist ID, playlist name, and customer ID
     *                              associated with it
     * @return updateBookInBooklistResult result object containing the API defined {@link BookModel}
     */
    public UpdateBookInBooklistResult handleRequest(final UpdateBookInBooklistRequest updateBookInBooklistRequest) {
        log.info("Received UpdateBookInBooklistRequest {}", updateBookInBooklistRequest);
        Book originalBook = bookDao.getBook(updateBookInBooklistRequest.getAsin());
        Book book = bookDao.getBook(updateBookInBooklistRequest.getAsin());
        //tries to update currently reading, leaves alone if null
        try {
            book.setCurrentlyReading(updateBookInBooklistRequest.isCurrentlyReading());
        } catch (NullPointerException e) {
            //can log here
        }
        //tries to update percent complete, leaves alone if null
        try {
            book.setPercentComplete(updateBookInBooklistRequest.getPercentComplete());
        } catch (NullPointerException e) {
            //can log here
        }
        List<Booklist> results = booklistDao.getAllBooklistsForUser(updateBookInBooklistRequest.getCustomerId());
        for (Booklist booklist : results) {
            List<Book> currentBooklist = booklist.getBooks();
            if (currentBooklist.contains(originalBook)) {
                List<Book> newlist = new ArrayList<>(currentBooklist);
                newlist.remove(originalBook);
                newlist.add(book);
                booklist.setBooks(newlist);
                booklistDao.saveBooklist(booklist);
            }
        }

        return UpdateBookInBooklistResult.builder()
                .withBook(new ModelConverterCarbon().toBookModel(book))
                .build();
    }

}

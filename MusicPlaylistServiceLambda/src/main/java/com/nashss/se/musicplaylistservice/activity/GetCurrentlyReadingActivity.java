package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.GetCurrentlyReadingRequest;
import com.nashss.se.musicplaylistservice.activity.results.GetCurrentlyReadingResult;
import com.nashss.se.musicplaylistservice.converters.ModelConverterCarbon;
import com.nashss.se.musicplaylistservice.dynamodb.BookDao;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Book;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.models.BooklistModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GetCurrentlyReadingActivity {
    private final Logger log = LogManager.getLogger();
    private final BookDao bookDao;
    private final BooklistDao booklistDao;

    /**
     * Instantiates a new GetCurrentlyReadingActivity object.
     *
     * @param bookDao BookDao to access the book table.
     * @param booklistDao BooklistDao to access the booklist table.
     */
    @Inject
    public GetCurrentlyReadingActivity(BookDao bookDao, BooklistDao booklistDao) {
        this.bookDao = bookDao;
        this.booklistDao = booklistDao;
    }

    /**
     * This method handles the incoming request by retrieving the books from the database.
     * <p>
     * It then returns the booklist.
     * <p>
     * If the booklist does not exist, this should throw a BooklistNotFoundException.
     *
     * @param getCurrentlyReadingRequest request object containing the boolean for reading and optional playlist id
     * @return getCurrentlyReadingResult result object containing the API defined {@link BooklistModel}
     */
    public GetCurrentlyReadingResult handleRequest(final GetCurrentlyReadingRequest getCurrentlyReadingRequest) {
        log.info("Received GetCurrentlyReadingRequest {}", getCurrentlyReadingRequest);
        BooklistModel booklistModel;
        boolean requestedBoolean = getCurrentlyReadingRequest.isCurrentlyReading();
        Booklist currentlyReading = bookDao.getCurrentlyReading(requestedBoolean);
        //narrows search results to a specific playlist. If no playlist given, returns all results.
        if (getCurrentlyReadingRequest.getId() != null) {
            Booklist requestedList = booklistDao.getBooklist(getCurrentlyReadingRequest.getId());
            List<Book> requestListBooks = requestedList.getBooks();
            List<Book> booksBeingRead = new ArrayList<>();
            for (Book book : requestListBooks) {
                if (currentlyReading.getBooks().contains(book)) {
                    booksBeingRead.add(book);
                }
            }
            requestedList.setBooks(booksBeingRead);
            booklistModel = new ModelConverterCarbon().toBooklistModel(requestedList);
        } else {
            booklistModel = new ModelConverterCarbon().toBooklistModel(currentlyReading);
        }

        return GetCurrentlyReadingResult.builder()
                .withBooklist(booklistModel)
                .build();
    }
}

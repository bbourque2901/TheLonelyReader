package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.GetBookFromBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.GetBookFromBooklistResult;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Book;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.exceptions.BookNotFoundException;
import com.nashss.se.musicplaylistservice.models.BookModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import javax.inject.Inject;

public class GetBookFromBooklistActivity {

    private final Logger log = LogManager.getLogger();
    private final BooklistDao booklistDao;

    /**
     * Instantiates a new GetBookFromBooklistActivity object.
     *
     * @param booklistDao BooklistDao to access the booklist table.
     */
    @Inject
    public GetBookFromBooklistActivity(BooklistDao booklistDao) {
        this.booklistDao = booklistDao;
    }

    /**
     * This method handles the incoming request by retrieving the user's booklists from the database.
     * <p>
     * It then returns the corresponding book from the booklist using the asin.
     * <p>
     * If the booklist does not exist, should throw BooklistNotFoundException.
     * If the book does not exist, should throw BookNotFoundException.
     *
     * @param getBookFromBooklistRequest request object containing the booklistId and bookAsin
     * @return getBookFromBooklistResult result object containing the API defined {@link BookModel}
     */
    public GetBookFromBooklistResult handleRequest(GetBookFromBooklistRequest getBookFromBooklistRequest) {
        log.info("Recieved GetBookFromBooklistRequest {}", getBookFromBooklistRequest);
        String booklistId = getBookFromBooklistRequest.getBooklistId();
        Booklist reqBooklist = booklistDao.getBooklist(booklistId);
        List<Book> userBooksList = reqBooklist.getBooks();

        String bookAsin = getBookFromBooklistRequest.getBookAsin();
        Book expBook = null;
        for (Book book : userBooksList) {
            if (book.getAsin().equals(bookAsin)) {
                expBook = book;
            }
        }

        if (expBook == null) {
            throw new BookNotFoundException("Requested book is not currently in user's booklist list of books.");
        }

        return GetBookFromBooklistResult.builder()
                .withBook(expBook)
                .build();
    }
}

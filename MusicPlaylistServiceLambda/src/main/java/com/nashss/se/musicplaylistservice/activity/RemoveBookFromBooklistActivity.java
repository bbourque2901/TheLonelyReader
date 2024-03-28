package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.RemoveBookFromBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.RemoveBookFromBooklistResult;
import com.nashss.se.musicplaylistservice.converters.ModelConverterCarbon;
import com.nashss.se.musicplaylistservice.dynamodb.BookDao;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Book;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.models.BookModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the RemoveBookFromBooklistActivity for the BookPlaylist API.
 *
 * This API allows the customer to remove one of their saved books in a booklist.
 */
public class RemoveBookFromBooklistActivity {
    private final Logger log = LogManager.getLogger();
    private final BookDao bookDao;
    private final BooklistDao booklistDao;

    /**
     * Instantiates a new RemoveBookFromBooklistActivity object.
     *
     * @param booklistDao BooklistDao to access the booklists table.
     * @param bookDao BookDao to access the book table.
     */
    @Inject
    public RemoveBookFromBooklistActivity(BooklistDao booklistDao, BookDao bookDao) {
        this.booklistDao = booklistDao;
        this.bookDao = bookDao;
    }

    /**
     * This method handles the incoming request by removing a book
     * from a book list and persisting the updated book list.
     * <p>
     * It then returns the updated list of books from the book list.
     * <p>
     * If the booklist does not exist, this should throw a BooklistNotFoundException.
     * <p>
     * If the book does not exist, this should throw an BookNotFoundException.
     *
     * @param removeBookFromBooklistRequest request object containing the book list ID and an asin to retrieve
     *                                 book data
     * @return removeBookFromPlaylistResult result object containing the book list's updated list of
     *                                 API defined {@link BookModel}s
     */
    public RemoveBookFromBooklistResult handleRequest(final RemoveBookFromBooklistRequest removeBookFromBooklistRequest) {
        log.info("Received RemoveBookFromBooklistRequest {} ", removeBookFromBooklistRequest);

        Booklist booklist = booklistDao.getBooklist(removeBookFromBooklistRequest.getId());

        if (!booklist.getCustomerId().equals(removeBookFromBooklistRequest.getCustomerId())) {
            throw new SecurityException("You must own a booklist to remove books from it!");
        }

        List<String> asins = booklist.getAsins();
        asins.remove(removeBookFromBooklistRequest.getAsin());

        List<Book> books = new ArrayList<>();
        for (String asin : asins) {
            books.remove(bookDao.getBook(asin));
        }

        booklist.setAsins(asins);
        booklist.setBookCount(asins.size());
        booklistDao.saveBooklist(booklist);

        List<BookModel> bookModels = new ModelConverterCarbon().toBookModelList(books);

        return RemoveBookFromBooklistResult.builder()
                .withBooklist(bookModels)
                .build();
    }
}

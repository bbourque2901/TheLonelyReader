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

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

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
     * @param removeBFromBooklistRequest request object containing the book list ID and an asin to retrieve
     *                                 book data
     * @return removeBookFromPlaylistResult result object containing the book list's updated list of
     *                                 API defined {@link BookModel}s
     */
    public RemoveBookFromBooklistResult handleRequest(final RemoveBookFromBooklistRequest removeBFromBooklistRequest) {
        log.info("Received RemoveBookFromBooklistRequest {} ", removeBFromBooklistRequest);

        Booklist booklist = booklistDao.getBooklist(removeBFromBooklistRequest.getId());

        if (!booklist.getCustomerId().equals(removeBFromBooklistRequest.getCustomerId())) {
            throw new SecurityException("You must own a booklist to remove books from it!");
        }

        Book bookToRemove = bookDao.getBook(removeBFromBooklistRequest.getAsin());

        List<Book> books = booklist.getBooks();
        books.remove(bookToRemove);

        booklist.setBooks(books);
        booklist.setBookCount(booklist.getBooks().size());
        booklist = booklistDao.saveBooklist(booklist);

        List<BookModel> bookModels = new ModelConverterCarbon().toBookModelList(booklist.getBooks());

        return RemoveBookFromBooklistResult.builder()
                .withBooklist(bookModels)
                .build();
    }
}

package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.AddBookToBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.AddBookToBooklistResult;
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
 * Implementation of the AddBookToBooklistActivity for LonelyRead's AddBookToBooklist API.
 *
 * This API allows the customer to add a book to their existing booklist.
 */
public class AddBookToBooklistActivity {
    private final Logger log = LogManager.getLogger();
    private final BookDao bookDao;
    private final BooklistDao booklistDao;

    /**
     * Instantiates a new AddSongToPlaylistActivity object.
     *
     * @param booklistDao BooklistDao to access the book_list table.
     * @param bookDao BookDao to access the book table.
     */
    @Inject
    public AddBookToBooklistActivity(BooklistDao booklistDao, BookDao bookDao) {
        this.bookDao = bookDao;
        this.booklistDao = booklistDao;
    }

    /**
     * This method handles the incoming request by adding a book
     * to a book list and persisting the updated book list.
     * <p>
     * It then returns the updated list of books from the book list.
     * <p>
     * If the booklist does not exist, this should throw a BooklistNotFoundException.
     * <p>
     * If the book does not exist, this should throw an BookNotFoundException.
     *
     * @param addBookToBooklistRequest request object containing the book list ID and an asin to retrieve
     *                                 book data
     * @return addBookToPlaylistResult result object containing the book list's updated list of
     *                                 API defined {@link BookModel}s
     */
    public AddBookToBooklistResult handleRequest(final AddBookToBooklistRequest addBookToBooklistRequest) {
        log.info("Received AddBookToBooklistRequest {} ", addBookToBooklistRequest);

        Booklist booklist = booklistDao.getBooklist(addBookToBooklistRequest.getId());

        if (!booklist.getCustomerId().equals(addBookToBooklistRequest.getCustomerId())) {
            throw new SecurityException("You must own a booklist to add books to it.");
        }

        List<String> asinsToAdd = new ArrayList<>();
        asinsToAdd.add(addBookToBooklistRequest.getAsin());

        List<Book> books = booklist.getBooks();
        for (String asin : asinsToAdd) {
            books.add(bookDao.getBook(asin));
        }

        booklist.setBooks(books);
        booklist.setBookCount(books.size());
        booklistDao.saveBooklist(booklist);

        List<BookModel> bookModels = new ModelConverterCarbon().toBookModelList(books);
        return AddBookToBooklistResult.builder()
                .withBooklist(bookModels)
                .build();
    }
}

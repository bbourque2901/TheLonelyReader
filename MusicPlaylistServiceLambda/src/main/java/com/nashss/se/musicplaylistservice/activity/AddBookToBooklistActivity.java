package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.AddBookToBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.AddBookToBooklistResult;
import com.nashss.se.musicplaylistservice.converters.ModelConverterCarbon;
import com.nashss.se.musicplaylistservice.dynamodb.BookDao;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Book;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.exceptions.GoogleBookAPISearchException;
import com.nashss.se.musicplaylistservice.googlebookapi.Request;
import com.nashss.se.musicplaylistservice.models.BookModel;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.books.v1.model.Volume;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private Request googleBookApi;

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

        // First, check if the book already exists in DynamoDB (searching by asin)
        // If not ('bookToAdd' = null), query the google book api for a book
        // It will take any search term, including an isbn(asin).
        // Both are just strings, so dont need to refactor yet

        // 1. queryBooks() method takes the search term (or asin) and returns a list of volumes (books) in JSON format
        // 2. extractAttributes() method selects a volume at a given index and extracts attributes needed
        //    for Book object (just first result for now). Returns JSON response of Book object to be created
        // 3. deserializeVolumeToBook() method takes JSON response returned from extractAttributes() and returns actual
        //    Book object with those attributes. Accepts any book JSON response

        Book bookToAdd = bookDao.getBook(addBookToBooklistRequest.getAsin());
        if (bookToAdd == null) {
            try {
                JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
                googleBookApi = new Request();
                List<Volume> volumes = googleBookApi.queryBooks(jsonFactory, addBookToBooklistRequest.getAsin());
                bookToAdd = googleBookApi.deserializeVolumeToBook(googleBookApi.extractAttributes(volumes, 0));
            } catch (Exception e) {
                throw new GoogleBookAPISearchException("Error with request to Google Book API", e);
            }
        }

        List<Book> books = booklist.getBooks();
        books.add(bookToAdd);

        booklist.setBooks(books);
        booklist.setBookCount(books.size());
        booklistDao.saveBooklist(booklist);

        List<BookModel> bookModels = new ModelConverterCarbon().toBookModelList(books);
        return AddBookToBooklistResult.builder()
                .withBooklist(bookModels)
                .build();
    }
}

package com.nashss.se.musicplaylistservice.converters;

import com.nashss.se.musicplaylistservice.dynamodb.models.Book;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.models.BookModel;
import com.nashss.se.musicplaylistservice.models.BooklistModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts between Data and API models.
 */
public class ModelConverterCarbon {
    /**
     * Converts a provided {@link Booklist} into a {@link BooklistModel} representation.
     *
     * @param bookList the book list to convert
     * @return the converted book list
     */
    public BooklistModel toBooklistModel(Booklist bookList) {
        List<String> tags = null;
        if (bookList.getTags() != null) {
            tags = new ArrayList<>(bookList.getTags());
        }

        List<Book> books = null;
        if (bookList.getBooks() != null) {
            books = new ArrayList<>(bookList.getBooks());
        }

        return BooklistModel.builder()
                .withId(bookList.getId())
                .withName(bookList.getName())
                .withCustomerId(bookList.getCustomerId())
                .withBookCount(bookList.getBookCount())
                .withTags(tags)
                .withBooks(books)
                .build();
    }

    /**
     * Converts a provided Book into a BookModel representation.
     *
     * @param book the Book to convert to BookModel
     * @return the converted BookModel with fields mapped from Book
     */
    public BookModel toBookModel(Book book) {
        return BookModel.builder()
                .withAsin(book.getAsin())
                .withTitle(book.getTitle())
                .withAuthor(book.getAuthor())
                .withGenre(book.getGenre())
                .withCurrentlyReading(book.isCurrentlyReading())
                .withPercentComplete(book.getPercentComplete())
                .withRating(book.getRating())
                .withComments(book.getComments())
                .build();
    }

    /**
     * Converts a list of Books to a list of BookModels.
     *
     * @param books The Books to convert to BookModels
     * @return The converted list of BookModels
     */
    public List<BookModel> toBookModelList(List<Book> books) {
        List<BookModel> bookModels = new ArrayList<>();

        for (Book book : books) {
            bookModels.add(toBookModel(book));
        }
        return bookModels;
    }

    /**
     * Converts a list of Booklists to a list of BooklistModels.
     *
     * @param bookLists The Booklists to convert to BooklistModels
     * @return The converted list of BooklistModels
     */
    public List<BooklistModel> toBookListModelList(List<Booklist> bookLists) {
        List<BooklistModel> bookListModels = new ArrayList<>();

        for (Booklist bookList : bookLists) {
            bookListModels.add(toBooklistModel(bookList));
        }

        return bookListModels;
    }
}

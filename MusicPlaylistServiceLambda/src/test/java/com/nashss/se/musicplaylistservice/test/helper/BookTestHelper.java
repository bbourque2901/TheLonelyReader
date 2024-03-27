package com.nashss.se.musicplaylistservice.test.helper;

import com.nashss.se.musicplaylistservice.dynamodb.models.AlbumTrack;
import com.nashss.se.musicplaylistservice.dynamodb.models.Book;
import com.nashss.se.musicplaylistservice.models.BookModel;
import com.nashss.se.musicplaylistservice.models.SongModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class BookTestHelper {
    private BookTestHelper() {
    }

    public static Book generateBook(int sequenceNumber) {
        Book book = new Book();
        book.setAsin("asin" + sequenceNumber);
        book.setTitle("title" + sequenceNumber);
        book.setAuthor("author" + sequenceNumber);
        book.setGenre("genre" + sequenceNumber);
        return book;
    }

    public static void assertBooksEqualBookModels(List<Book> books, List<BookModel> bookModels) {
        assertEquals(books.size(),
                     bookModels.size(),
                     String.format("Expected books (%s) and book models (%s) to match",
                                   books,
                                   bookModels));
        for (int i = 0; i < books.size(); i++) {
            assertBookEqualsBookModel(
                books.get(i),
                bookModels.get(i),
                String.format("Expected %dth book (%s) to match corresponding book model (%s)",
                              i,
                              books.get(i),
                              bookModels.get(i)));
        }
    }

    public static void assertBookEqualsBookModel(Book book, BookModel bookModel) {
        String message = String.format("Expected book %s to match book model %s", book, bookModel);
        assertBookEqualsBookModel(book, bookModel, message);
    }

    public static void assertBookEqualsBookModel(Book book, BookModel bookModel, String message) {
        assertEquals(book.getAsin(), bookModel.getAsin(), message);
        assertEquals(book.getTitle(), bookModel.getTitle(), message);
        assertEquals(book.getAuthor(), bookModel.getAuthor(), message);
        assertEquals(book.getGenre(), bookModel.getGenre(), message);
    }
}

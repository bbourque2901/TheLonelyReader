package com.nashss.se.booktrackerservice.test.helper;

import com.nashss.se.booktrackerservice.dynamodb.models.Book;
import com.nashss.se.booktrackerservice.dynamodb.models.Booklist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class BooklistTestHelper {
    private BooklistTestHelper() {
    }

    public static Booklist generateBooklist() {
        return generateBooklistWithNBooks(1);
    }

    public static Booklist generateBooklistWithNBooks(int numBooks) {
        Booklist booklist = new Booklist();
        booklist.setId("id");
        booklist.setName("a booklist");
        booklist.setCustomerId("customerABC");
        booklist.setTags(Collections.singleton("tag"));

        List<Book> asins = new ArrayList<>();
        for (int i = 0; i < numBooks; i++) {
            asins.add(BookTestHelper.generateBook(i));
        }
        booklist.setBooks(asins);
        booklist.setBookCount(asins.size());

        return booklist;
    }
}

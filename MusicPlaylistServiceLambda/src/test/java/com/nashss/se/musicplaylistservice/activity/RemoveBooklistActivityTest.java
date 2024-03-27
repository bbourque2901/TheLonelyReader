package com.nashss.se.musicplaylistservice.activity;

import com.google.common.collect.Sets;
import com.nashss.se.musicplaylistservice.activity.requests.RemoveBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.RemoveBooklistResult;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RemoveBooklistActivityTest {
    @Mock
    private BooklistDao booklistDao;

    private RemoveBooklistActivity removeBooklistActivity;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        removeBooklistActivity = new RemoveBooklistActivity(booklistDao);
    }

    @Test
    public void handleRequest_savedBooklistFound_returnsRemovedBooklistModelInResult() {
        // GIVEN
        String expectedId = "expectedId";
        String expectedName = "expectedName";
        String expectedCustomerId = "expectedCustomerId";
        int expectedBookCount = 0;
        List<String> expectedTags = List.of("tag");

        Booklist booklist = new Booklist();
        booklist.setId(expectedId);
        booklist.setName(expectedName);
        booklist.setCustomerId(expectedCustomerId);
        booklist.setBookCount(expectedBookCount);
        booklist.setTags(Sets.newHashSet(expectedTags));

        when(booklistDao.removeBooklist(expectedId)).thenReturn(booklist);

        RemoveBooklistRequest request = RemoveBooklistRequest.builder()
                .withId(expectedId)
                .build();

        // WHEN
        RemoveBooklistResult result = removeBooklistActivity.handleRequest(request);

        // THEN
        assertEquals(expectedId, result.getBooklist().getId());
        assertEquals(expectedName, result.getBooklist().getName());
        assertEquals(expectedCustomerId, result.getBooklist().getCustomerId());
        assertEquals(expectedBookCount, result.getBooklist().getBookCount());
        assertEquals(expectedTags, result.getBooklist().getTags());

    }

    @Test
    public void handleRequest_removedBooklist_removedFromBooklists() {
        //GIVEN
        String expectedId = "expectedId";

        when(booklistDao.removeBooklist(expectedId)).thenReturn(null);

        RemoveBooklistRequest request = RemoveBooklistRequest.builder()
                .withId(expectedId)
                .build();

        //WHEN
        RemoveBooklistResult result = removeBooklistActivity.handleRequest(request);

        //THEN
        assertNull(result.getBooklist());
    }
}

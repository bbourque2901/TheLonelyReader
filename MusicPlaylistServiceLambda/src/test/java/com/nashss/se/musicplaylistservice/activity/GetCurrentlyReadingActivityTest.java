package com.nashss.se.musicplaylistservice.activity;

import com.google.common.collect.Sets;
import com.nashss.se.musicplaylistservice.activity.requests.GetBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.requests.GetCurrentlyReadingRequest;
import com.nashss.se.musicplaylistservice.activity.results.GetBooklistResult;
import com.nashss.se.musicplaylistservice.activity.results.GetCurrentlyReadingResult;
import com.nashss.se.musicplaylistservice.dynamodb.BookDao;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetCurrentlyReadingActivityTest {
    @Mock
    private BooklistDao booklistDao;
    @Mock
    private BookDao bookDao;

    private GetCurrentlyReadingActivity getCurrentlyReadingActivity;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        getCurrentlyReadingActivity = new GetCurrentlyReadingActivity(bookDao, booklistDao);
    }

    @Test
    public void handleRequest_savedBooklistFound_returnsBooklistModelInResult() {
        // GIVEN
        String expectedId = "expectedId";
        String expectedName = "expectedName";
        String expectedCustomerId = "expectedCustomerId";
        int expectedBookCount = 0;
        List<String> expectedTags = List.of("tag");
        List<String> asins = List.of("111", "222", "333", "current1", "current2", "444");

        Booklist booklist = new Booklist();
        booklist.setId(expectedId);
        booklist.setName(expectedName);
        booklist.setCustomerId(expectedCustomerId);
        booklist.setBookCount(expectedBookCount);
        booklist.setTags(Sets.newHashSet(expectedTags));
        booklist.setAsins(asins);

        Booklist currentlyReading = new Booklist();
        currentlyReading.setAsins(List.of("current1", "current2"));

        when(booklistDao.getBooklist(expectedId)).thenReturn(booklist);
        when(bookDao.getCurrentlyReading(true)).thenReturn(currentlyReading);

        GetCurrentlyReadingRequest request = GetCurrentlyReadingRequest.builder()
                .withCurrentlyReading(true)
                .withId(expectedId)
                .build();

        // WHEN
        GetCurrentlyReadingResult result = getCurrentlyReadingActivity.handleRequest(request);

        // THEN
        assertEquals(expectedId, result.getBooklist().getId());
        assertEquals(expectedName, result.getBooklist().getName());
        assertEquals(expectedCustomerId, result.getBooklist().getCustomerId());
        assertEquals(expectedBookCount, result.getBooklist().getBookCount());
        assertEquals(expectedTags, result.getBooklist().getTags());
        assertEquals(List.of("current1", "current2"), result.getBooklist().g);
    }
}

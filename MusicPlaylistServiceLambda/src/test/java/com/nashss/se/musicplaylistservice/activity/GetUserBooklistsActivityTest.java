package com.nashss.se.musicplaylistservice.activity;

import com.google.common.collect.Sets;
import com.nashss.se.musicplaylistservice.activity.requests.GetBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.requests.GetUserBooklistsRequest;
import com.nashss.se.musicplaylistservice.activity.results.GetBooklistResult;
import com.nashss.se.musicplaylistservice.activity.results.GetUserBooklistsResult;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.test.helper.BooklistTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetUserBooklistsActivityTest {

    @Mock
    private BooklistDao booklistDao;
    private GetUserBooklistsActivity getUserBooklistsActivity;

    @BeforeEach
    public void setup() {
        initMocks(this);
        getUserBooklistsActivity = new GetUserBooklistsActivity(booklistDao);
    }

    @Test
    public void handleRequest_savedBooklistsFound_returnsListOfBooklistModelInResult() {
        // GIVEN
        Booklist booklist1 = BooklistTestHelper.generateBooklistWithNBooks(5);
        Booklist booklist2 = BooklistTestHelper.generateBooklistWithNBooks(3);
        List<Booklist> expectedBooklists = List.of(booklist1, booklist1);

        when(booklistDao.getAllBooklistsForUser(booklist1.getCustomerId())).thenReturn(expectedBooklists);

        GetUserBooklistsRequest request = GetUserBooklistsRequest.builder()
                .withCustomerId(booklist1.getCustomerId())
                .build();

        // WHEN
        GetUserBooklistsResult result = getUserBooklistsActivity.handleRequest(request);

        // THEN
        assertTrue(result.getBooklists().size() == 2);
    }
}

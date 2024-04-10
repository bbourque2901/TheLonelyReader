package com.nashss.se.booktrackerservice.activity;

import com.nashss.se.booktrackerservice.activity.requests.GetUserBooklistsRequest;
import com.nashss.se.booktrackerservice.activity.results.GetUserBooklistsResult;
import com.nashss.se.booktrackerservice.dynamodb.BooklistDao;
import com.nashss.se.booktrackerservice.dynamodb.models.Booklist;
import com.nashss.se.booktrackerservice.test.helper.BooklistTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
        Booklist booklist2 = BooklistTestHelper.generateBooklistWithNBooks(10);


        List<Booklist> expectedBooklists = List.of(booklist1, booklist2);

        when(booklistDao.getAllBooklistsForUser(booklist1.getCustomerId())).thenReturn(expectedBooklists);

        GetUserBooklistsRequest request = GetUserBooklistsRequest.builder()
                .withCustomerId(booklist1.getCustomerId())
                .build();

        // WHEN
        GetUserBooklistsResult result = getUserBooklistsActivity.handleRequest(request);

        // THEN
        assertTrue(result.getBooklists().size() == 2);
        assertNotNull(result);
        assertEquals(result.getBooklists().get(0).getBookCount(), booklist1.getBookCount());
        assertEquals(result.getBooklists().get(1).getBookCount(), booklist2.getBookCount());
    }
}

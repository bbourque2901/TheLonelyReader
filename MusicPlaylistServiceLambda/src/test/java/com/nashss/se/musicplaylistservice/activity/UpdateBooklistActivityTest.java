package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.UpdateBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.UpdateBooklistResult;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.exceptions.BooklistNotFoundException;
import com.nashss.se.musicplaylistservice.metrics.MetricsConstants;
import com.nashss.se.musicplaylistservice.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class UpdateBooklistActivityTest {

    @Mock
    private BooklistDao booklistDao;
    @Mock
    private MetricsPublisher metricsPublisher;
    private UpdateBooklistActivity updateBooklistActivity;

    @BeforeEach
    public void setup() {
        openMocks(this);
        updateBooklistActivity = new UpdateBooklistActivity(booklistDao, metricsPublisher);
    }

    @Test
    public void handleRequest_goodRequest_updatesBooklistName() {
        // GIVEN
        String id = "id";
        String expectedCustomerId = "expectedCustomerId";
        String expectedName = "new name";
        int expectedBookCount = 5;

        UpdateBooklistRequest request = UpdateBooklistRequest.builder()
                .withId(id)
                .withCustomerId(expectedCustomerId)
                .withName(expectedName)
                .build();

        Booklist startingBooklist = new Booklist();
        startingBooklist.setCustomerId(expectedCustomerId);
        startingBooklist.setName("old name");
        startingBooklist.setBookCount(expectedBookCount);

        when(booklistDao.getBooklist(id)).thenReturn(startingBooklist);
        when(booklistDao.saveBooklist(startingBooklist)).thenReturn(startingBooklist);

        // WHEN
        UpdateBooklistResult result = updateBooklistActivity.handleRequest(request);

        // THEN
        assertEquals(expectedName, result.getBooklist().getName());
        assertEquals(expectedCustomerId, result.getBooklist().getCustomerId());
        assertEquals(expectedBookCount, result.getBooklist().getBookCount());
    }

    @Test
    public void handleRequest_booklistDoesNotExist_throwsBooklistNotFoundException() {
        // GIVEN
        String id = "id";
        UpdateBooklistRequest request = UpdateBooklistRequest.builder()
                .withId(id)
                .withName("name")
                .withCustomerId("customerId")
                .build();

        when(booklistDao.getBooklist(id)).thenThrow(new BooklistNotFoundException());

        // THEN
        assertThrows(BooklistNotFoundException.class, () -> updateBooklistActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_customerIdNotMatch_throwsSecurityException() {
        // GIVEN
        String id = "id";
        UpdateBooklistRequest request = UpdateBooklistRequest.builder()
                .withId(id)
                .withName("name")
                .withCustomerId("customerId")
                .build();

        Booklist differentCustomerIdBooklist = new Booklist();
        differentCustomerIdBooklist.setCustomerId("different");

        when(booklistDao.getBooklist(id)).thenReturn(differentCustomerIdBooklist);

        // WHEN + THEN
        try {
            updateBooklistActivity.handleRequest(request);
            fail("Expected InvalidAttributeChangeException to be thrown");
        } catch (SecurityException e) {
            verify(metricsPublisher).addCount(MetricsConstants.UPDATEBOOKLIST_INVALIDATTRIBUTEVALUE_COUNT, 0);
            verify(metricsPublisher).addCount(MetricsConstants.UPDATEBOOKLIST_INVALIDATTRIBUTECHANGE_COUNT, 1);
        }
    }
}

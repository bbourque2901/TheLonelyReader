package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.UpdateBooklistRequest;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

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

    }
}

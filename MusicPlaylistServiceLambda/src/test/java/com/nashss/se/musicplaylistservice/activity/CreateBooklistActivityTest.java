package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.CreateBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.CreateBooklistResult;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.exceptions.InvalidAttributeValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class CreateBooklistActivityTest {
    @Mock
    private BooklistDao booklistDao;

    private CreateBooklistActivity createBooklistActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        createBooklistActivity = new CreateBooklistActivity(booklistDao);
    }

    @Test
    public void handleRequest_withTags_createsAndSavesBooklistWithTags() {
        // GIVEN
        String expectedName = "expectedName";
        String expectedCustomerId = "expectedCustomerId";
        int expectedBookCount = 0;
        List<String> expectedTags = List.of("tag");

        CreateBooklistRequest request = CreateBooklistRequest.builder()
                .withName(expectedName)
                .withCustomerId(expectedCustomerId)
                .withTags(expectedTags)
                .build();

        // WHEN
        CreateBooklistResult result = createBooklistActivity.handleRequest(request);

        // THEN
        verify(booklistDao).saveBooklist(any(Booklist.class));

        assertNotNull(result.getBooklist().getId());
        assertEquals(expectedName, result.getBooklist().getName());
        assertEquals(expectedCustomerId, result.getBooklist().getCustomerId());
        assertEquals(expectedBookCount, result.getBooklist().getBookCount());
        assertEquals(expectedTags, result.getBooklist().getTags());
    }

    @Test
    public void handleRequest_noTags_createsAndSavesBooklistWithoutTags() {
        // GIVEN
        String expectedName = "expectedName";
        String expectedCustomerId = "expectedCustomerId";
        int expectedBookCount = 0;

        CreateBooklistRequest request = CreateBooklistRequest.builder()
                .withName(expectedName)
                .withCustomerId(expectedCustomerId)
                .build();

        // WHEN
        CreateBooklistResult result = createBooklistActivity.handleRequest(request);

        // THEN
        verify(booklistDao).saveBooklist(any(Booklist.class));

        assertNotNull(result.getBooklist().getId());
        assertEquals(expectedName, result.getBooklist().getName());
        assertEquals(expectedCustomerId, result.getBooklist().getCustomerId());
        assertEquals(expectedBookCount, result.getBooklist().getBookCount());
        assertNull(result.getBooklist().getTags());
    }

    @Test
    public void handleRequest_invalidName_throwsInvalidAttributeValueException() {
        // GIVEN
        CreateBooklistRequest request = CreateBooklistRequest.builder()
                .withName("I'm illegal")
                .withCustomerId("customerId")
                .build();

        // WHEN + THEN
        assertThrows(InvalidAttributeValueException.class, () -> createBooklistActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_invalidCustomerId_throwsInvalidAttributeValueException() {
        // GIVEN
        CreateBooklistRequest request = CreateBooklistRequest.builder()
                .withName("AllOK")
                .withCustomerId("Jemma's \"illegal\" customer ID")
                .build();

        // WHEN + THEN
        assertThrows(InvalidAttributeValueException.class, () -> createBooklistActivity.handleRequest(request));
    }
}

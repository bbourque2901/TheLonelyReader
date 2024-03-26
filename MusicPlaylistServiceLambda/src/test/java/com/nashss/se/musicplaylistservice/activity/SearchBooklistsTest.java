package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.SearchBooklistsRequest;
import com.nashss.se.musicplaylistservice.activity.results.SearchBooklistsResult;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.models.BooklistModel;

import com.google.common.collect.Sets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SearchBooklistsTest {

    @Mock
    private BooklistDao booklistDao;
    private SearchBooklistsActivity searchBooklistsActivity;

    @BeforeEach
    public void setup() {
        initMocks(this);
        searchBooklistsActivity = new SearchBooklistsActivity(booklistDao);
    }

    @Test
    public void handleRequest_whenBooklistsMatchSearch_returnsBooklistModelListInResult() {
        // GIVEN
        String criteria = "good";
        String[] criteriaArray = {criteria};

        List<Booklist> expected = List.of(
                newBooklist("id1", "a good playlist", List.of("tag1", "tag2")),
                newBooklist("id2", "another good playlist", List.of("tag1", "tag2")));

        when(booklistDao.searchBooklists(criteriaArray)).thenReturn(expected);

        SearchBooklistsRequest request = SearchBooklistsRequest.builder()
                .withCriteria(criteria)
                .build();

        // WHEN
        SearchBooklistsResult result = searchBooklistsActivity.handleRequest(request);

        // THEN
        List<BooklistModel> resultBooklists = result.getBooklists();
        assertEquals(expected.size(), resultBooklists.size());

        for (int i=0; i<expected.size(); i++) {
            assertEquals(expected.get(i).getId(), resultBooklists.get(i).getId());
            assertEquals(expected.get(i).getName(), resultBooklists.get(i).getName());
        }
    }

    @Test
    public void handleRequest_withNullCriteria_isIdenticalToEmptyCriteria() {
        // GIVEN
        String criteria = null;
        ArgumentCaptor<String[]> criteriaArray = ArgumentCaptor.forClass(String[].class);

        when(booklistDao.searchBooklists(criteriaArray.capture())).thenReturn(List.of());

        SearchBooklistsRequest request = SearchBooklistsRequest.builder()
                .withCriteria(criteria)
                .build();

        // WHEN
        SearchBooklistsResult result = searchBooklistsActivity.handleRequest(request);

        // THEN
        assertEquals(0, criteriaArray.getValue().length, "Criteria Array should be empty");
    }

    private static Booklist newBooklist(String id, String name, List<String> tags) {
        Booklist booklist = new Booklist();

        booklist.setId(id);
        booklist.setName(name);
        booklist.setTags(Sets.newHashSet(tags));

        // the test code doesn't need these properties to be distinct.
        booklist.setCustomerId("a customer id");
        booklist.setBookCount(0);

        return booklist;
    }
}

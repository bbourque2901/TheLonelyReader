package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.GetBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.GetBooklistResult;
import com.nashss.se.musicplaylistservice.converters.ModelConverterCarbon;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.models.BooklistModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Implementation of the GetBooklistActivity for the BookPlaylist API.
 *
 * This API allows the customer to get one of their saved booklists.
 */
public class GetBooklistActivity {
    private final Logger log = LogManager.getLogger();
    private final BooklistDao booklistDao;

    /**
     * Instantiates a new GetBooklistActivity object.
     *
     * @param booklistDao BooklistDao to access the booklist table.
     */
    @Inject
    public GetBooklistActivity(BooklistDao booklistDao) {
        this.booklistDao = booklistDao;
    }

    /**
     * This method handles the incoming request by retrieving the booklist from the database.
     * <p>
     * It then returns the booklist.
     * <p>
     * If the booklist does not exist, this should throw a BooklistNotFoundException.
     *
     * @param getBooklistRequest request object containing the playlist ID
     * @return getBooklistResult result object containing the API defined {@link BooklistModel}
     */
    public GetBooklistResult handleRequest(final GetBooklistRequest getBooklistRequest) {
        log.info("Received GetBooklistRequest {}", getBooklistRequest);
        String requestedId = getBooklistRequest.getId();
        Booklist booklist = booklistDao.getBooklist(requestedId);
        BooklistModel booklistModel = new ModelConverterCarbon().toBookListModel(booklist);

        return GetBooklistResult.builder()
                .withBooklist(booklistModel)
                .build();
    }
}

package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.GetCurrentlyReadingRequest;
import com.nashss.se.musicplaylistservice.activity.results.GetCurrentlyReadingResult;
import com.nashss.se.musicplaylistservice.converters.ModelConverterCarbon;
import com.nashss.se.musicplaylistservice.dynamodb.BookDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.models.BooklistModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class GetCurrentlyReadingActivity {
    private final Logger log = LogManager.getLogger();
    private final BookDao bookDao;

    /**
     * Instantiates a new GetCurrentlyReadingActivity object.
     *
     * @param bookDao BookDao to access the book table.
     */
    @Inject
    public GetCurrentlyReadingActivity(BookDao bookDao) { this.bookDao = bookDao;}

    /**
     * This method handles the incoming request by retrieving the books from the database.
     * <p>
     * It then returns the booklist.
     * <p>
     * If the booklist does not exist, this should throw a BooklistNotFoundException.
     *
     * @param getCurrentlyReadingRequest request object containing the boolean for reading
     * @return getCurrentlyReadingResult result object containing the API defined {@link BooklistModel}
     */
    public GetCurrentlyReadingResult handleRequest(final GetCurrentlyReadingRequest getCurrentlyReadingRequest) {
        log.info("Recieved GetCurrentlyReadingRequest {}", getCurrentlyReadingRequest);
        boolean requestedBoolean = getCurrentlyReadingRequest.isCurrentlyReading();
        Booklist booklist = bookDao.getCurrentlyReading(requestedBoolean);
        BooklistModel booklistModel = new ModelConverterCarbon().toBooklistModel(booklist);

        return GetCurrentlyReadingResult.builder()
                .withBooklist(booklistModel)
                .build();
    }
}

package com.nashss.se.booktrackerservice.activity;

import com.nashss.se.booktrackerservice.activity.requests.GetUserBooklistsRequest;
import com.nashss.se.booktrackerservice.activity.results.GetUserBooklistsResult;
import com.nashss.se.booktrackerservice.converters.ModelConverterCarbon;
import com.nashss.se.booktrackerservice.dynamodb.BooklistDao;
import com.nashss.se.booktrackerservice.dynamodb.models.Booklist;
import com.nashss.se.booktrackerservice.models.BooklistModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import javax.inject.Inject;

public class GetUserBooklistsActivity {

    private final Logger log = LogManager.getLogger();
    private final BooklistDao booklistDao;

    /**
     * Instantiates a new GetUserBooklistsActivity object.
     *
     * @param booklistDao BooklistDao to access the booklist table.
     */
    @Inject
    public GetUserBooklistsActivity(BooklistDao booklistDao) {
        this.booklistDao = booklistDao;
    }

    /**
     * This method handles the incoming request by retrieving the user's booklists from the database.
     * <p>
     * It then returns a list of booklists.
     * <p>
     * If the user does not exist, this should throw a UserNotFoundException.
     *
     * @param getUserBooklistsRequest request object containing the customer ID
     * @return getUserBooklistsResult result object containing the API defined {@link BooklistModel}
     */
    public GetUserBooklistsResult handleRequest(final GetUserBooklistsRequest getUserBooklistsRequest) {
        log.info("Recieved GetUserBooklistsRequest {}", getUserBooklistsRequest);
        String requestedCustomerId = getUserBooklistsRequest.getCustomerId();
        List<Booklist> userBooklists = booklistDao.getAllBooklistsForUser(requestedCustomerId);
        List<BooklistModel> userBooklistsModels = new ModelConverterCarbon().toBookListModelList(userBooklists);

        return GetUserBooklistsResult.builder()
                .withBooklists(userBooklistsModels)
                .build();
    }
}

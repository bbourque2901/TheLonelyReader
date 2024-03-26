package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.CreateBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.CreateBooklistResult;
import com.nashss.se.musicplaylistservice.activity.results.CreatePlaylistResult;
import com.nashss.se.musicplaylistservice.converters.ModelConverter;
import com.nashss.se.musicplaylistservice.converters.ModelConverterCarbon;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.dynamodb.models.Playlist;
import com.nashss.se.musicplaylistservice.exceptions.InvalidAttributeValueException;
import com.nashss.se.musicplaylistservice.models.BooklistModel;
import com.nashss.se.musicplaylistservice.models.PlaylistModel;
import com.nashss.se.projectresources.music.playlist.servic.util.MusicPlaylistServiceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of the CreateBooklistActivity for the MusicPlaylistService's CreateBooklist API.
 * <p>
 * This API allows the customer to create a new booklist with no books.
 */
public class CreateBooklistActivity {
    private final Logger log = LogManager.getLogger();
    private final BooklistDao booklistDao;

    /**
     * Instantiates a new CreateBooklistActivity object.
     *
     * @param booklistDao BooklistDao to access the playlists table.
     */
    @Inject
    public CreateBooklistActivity(BooklistDao booklistDao) {
        this.booklistDao = booklistDao;
    }
/**
 * This method handles the incoming request by persisting a new booklist
 * with the provided booklist name and customer ID from the request.
 * <p>
 * It then returns the newly created booklist.
 * <p>
 * If the provided booklist name or customer ID has invalid characters, throws an
 * InvalidAttributeValueException
 *
 * @param createBooklistRequest request object containing the booklist name and customer ID
 *                              associated with it
 * @return createBooklistResult result object containing the API defined {@link BooklistModel}
 */

public CreateBooklistResult handleRequest(final CreateBooklistRequest createBooklistRequest) {
    log.info("Received CreatePlaylistRequest {}", createBooklistRequest);

    if (!MusicPlaylistServiceUtils.isValidString(createBooklistRequest.getName())) {
        throw new InvalidAttributeValueException("Playlist name [" + createBooklistRequest.getName() +
                "] contains illegal characters");
    }

    if (!MusicPlaylistServiceUtils.isValidString(createBooklistRequest.getCustomerId())) {
        throw new InvalidAttributeValueException("Playlist customer ID [" + createBooklistRequest.getCustomerId() +
                "] contains illegal characters");
    }

    Set<String> booklistTags = null;
    if (createBooklistRequest.getTags() != null) {
        booklistTags = new HashSet<>(createBooklistRequest.getTags());
    }

    Booklist booklist = new Booklist();
    booklist.setId(MusicPlaylistServiceUtils.generatePlaylistId());
    booklist.setName(createBooklistRequest.getName());
    booklist.setCustomerId(createBooklistRequest.getCustomerId());
    booklist.setBookCount(0);
    booklist.setTags(booklistTags);
    booklist.setAsins(new ArrayList<>());

    booklistDao.saveBooklist(booklist);

    BooklistModel booklistModel = new ModelConverterCarbon().toBooklistModel(booklist);
    return CreateBooklistResult.builder()
            .withBooklist(booklistModel)
            .build();
}
}

package com.nashss.se.musicplaylistservice.googlebookapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.services.books.v1.Books;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.books.v1.model.Volume;
import com.google.api.services.books.v1.model.Volumes;
import com.nashss.se.musicplaylistservice.dynamodb.models.Book;
import com.nashss.se.musicplaylistservice.googlebookapi.helper.VolumeInfoHelper;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class Request {
    private static final String APPLICATION_NAME = "LonelyReads";

    // Example JSON response : https://www.googleapis.com/books/v1/volumes/btpIkZ6X6egC

    /**
     * Queries the Google Book API with a given search term
     * @param jsonFactory used to parse JSONs and access object properties
     * @param searchTerm the search term used to query the Google Book API
     * @return a list of Volumes returned by the query
     * @throws IOException when errors making request to the API
     * @throws GeneralSecurityException provides type safety
     */
    public List<Volume> queryBooks(JsonFactory jsonFactory, String searchTerm) throws IOException, GeneralSecurityException {

        Books books = new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
                .setApplicationName(APPLICATION_NAME)
                .build();

        Books.Volumes.List volumesList = books.volumes().list(searchTerm);

        //System.out.println(query);

        Volumes volumes = volumesList.execute();

        // If there are no results/Total results
        if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
            System.out.println("No matches found, try narrowing your search..");
        }

        return volumes.getItems();
    }

    /**
     * Extracts attributes needed to create Book object from a Volume.
     * @param volumes a list of Volumes returned from querying the Google Book API
     * @param desiredIndex the index of the desired Volume from list of Volumes
     * @return a JSON representation of the Book object to be created
     * @throws JsonProcessingException when a Volume has null isbn or title
     */
    public String extractAttributes(List<Volume> volumes, int desiredIndex) throws JsonProcessingException {
        // Get the volume's VolumeInfo object from JSON response
        // VolumeInfo contains the attributes needed for a Book object
        Volume.VolumeInfo volumeInfo = volumes.get(desiredIndex).getVolumeInfo();

        // Instantiate helper class to parse through VolumeInfo JSON
        VolumeInfoHelper helper = new VolumeInfoHelper();

        // Create Book object and set attributes as values extracted by helper class

        // For our ASIN, should we just use the ISBN?
        Book book = new Book();
        book.setAsin(helper.getIsbn(volumeInfo));
        book.setTitle(helper.getTitle(volumeInfo));
        if (!helper.getAuthors(volumeInfo).isEmpty()) {
            book.setAuthor(helper.getAuthors(volumeInfo).get(0));
        } else {
            book.setAuthor("");
        }
        if (!helper.getGenres(volumeInfo).isEmpty()) {
            book.setGenre(helper.getGenres(volumeInfo).get(0));
        } else {
            book.setGenre("");
        }
        book.setThumbnail(helper.getThumbnail(volumeInfo));
        //book.setPageCount(Integer.parseInt(helper.getPageCount(volumeInfo)));

        ObjectMapper mapper = new ObjectMapper();

        // Return the Book as JSON
        return mapper.writeValueAsString(book);
    }

    /**
     * Deserializes a JSON array to a Book object.
     * @param jsonArray JSON representation of a Book returned from extractAttributes()
     * @return a Book object
     * @throws JsonProcessingException when passing jsonArray with null isbn or title
     */
    public Book deserializeVolumeToBook(String jsonArray) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // Use JSON of Book created in extractAttributes() to return actual Book object
        return mapper.readValue(jsonArray, Book.class);
    }

}

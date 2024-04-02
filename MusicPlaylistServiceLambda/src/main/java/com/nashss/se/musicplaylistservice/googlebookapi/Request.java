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

import java.util.List;

public class Request {
    private static final String APPLICATION_NAME = "LonelyReads";

    // Example JSON response : https://www.googleapis.com/books/v1/volumes/btpIkZ6X6egC

    public List<Volume> queryBooks(JsonFactory jsonFactory, String query) throws Exception {

        Books books = new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
                .setApplicationName(APPLICATION_NAME)
                .build();

        Books.Volumes.List volumesList = books.volumes().list(query);

        System.out.println(query);

        Volumes volumes = volumesList.execute();

        // If there are no results/Total results
        if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
            System.out.println("No matches found, try narrowing your search..");
        }

        return volumes.getItems();
    }

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
        book.setAuthor(helper.getAuthors(volumeInfo).get(0));
        book.setGenre(helper.getGenres(volumeInfo).get(0));
        book.setThumbnail(helper.getThumbnail(volumeInfo));
        //book.setPageCount(Integer.parseInt(helper.getPageCount(volumeInfo)));

        ObjectMapper mapper = new ObjectMapper();

        // Return the Book as JSON
        return mapper.writeValueAsString(book);
    }

    public Book deserializeVolumeToBook(String jsonArray) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // Use JSON of Book created in extractAttributes() to return actual Book object
        return mapper.readValue(jsonArray, Book.class);
    }

}

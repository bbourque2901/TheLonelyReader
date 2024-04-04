package com.nashss.se.musicplaylistservice.googlebookapi.helper;

import com.google.api.services.books.v1.model.Volume;

import java.util.Collections;
import java.util.List;

public class VolumeInfoHelper {
    // Splitting this up may help with narrowing errors

    /**
     * Extracts the ISBN/ASIN attribute from a Volume's VolumeInfo object.
     * @param volumeInfo a Volume's VolumeInfo object
     * @return a Volume's ISBN as a String
     */
    public String getIsbn(Volume.VolumeInfo volumeInfo) {
        for (Volume.VolumeInfo.IndustryIdentifiers ii : volumeInfo.getIndustryIdentifiers()) {
            if (ii.getType().equals("ISBN_13")) {
                return ii.getIdentifier();
            } else if (ii.getType().equals("ISBN_10")) {
                return ii.getIdentifier();
            } else {
                return ii.getIdentifier();
            }
        }

        return "";
    }

    /**
     * Extracts the title attribute from a Volume's VolumeInfo object.
     * @param volumeInfo a Volume's VolumeInfo object
     * @return a Volume's title, with subtitle appended, as a String
     */
    public String getTitle(Volume.VolumeInfo volumeInfo) {
        if (volumeInfo.getSubtitle() != null) {
            return volumeInfo.getTitle() + ", " + volumeInfo.getSubtitle();
        } else {
            return volumeInfo.getTitle();
        }
    }

    /**
     * Extracts the author attribute from a Volume's VolumeInfo object.
     * @param volumeInfo a Volume's VolumeInfo objet
     * @return a Volume's author(s) as a list of Strings
     */
    public List<String> getAuthors(Volume.VolumeInfo volumeInfo) {
        if (volumeInfo.getAuthors() != null) {
            return volumeInfo.getAuthors();
        }

        return Collections.emptyList();
    }

    /**
     * Extracts the genre attribute from a Volume's VolumeInfo object.
     * @param volumeInfo a Volume's VolumeInfo object
     * @return a Volume's genre(s) as a list of Strings
     */
    public List<String> getGenres(Volume.VolumeInfo volumeInfo) {
        if (volumeInfo.getCategories() != null) {
            return volumeInfo.getCategories();
        }

        return Collections.emptyList();
    }

    /**
     * Extracts the thumbnail attribute from a Volume's VolumeInfo object.
     * @param volumeInfo a Volume's VolumeInfo object
     * @return a Volume's thumbnail url as a String
     */
    public String getThumbnail(Volume.VolumeInfo volumeInfo) {
        if (volumeInfo.getImageLinks().get("thumbnail") != null) {
            return volumeInfo.getImageLinks().get("thumbnail").toString();
        }

        return "";
    }

    /**
     * Extracts the pageCount attribute from a Volume's VolumeInfo.
     * @param volumeInfo a Volume's VolumeInfo object
     * @return a Volume's pageCount as a String
     */
    public String getPageCount(Volume.VolumeInfo volumeInfo) {
        if (volumeInfo.getPageCount() != null) {
            return volumeInfo.getPageCount().toString();
        }

        return "";
    }

    /**
     * Displays a Volume from a list of Volumes at a given index.
     * @param volumes a list of Volumes returned from querying the Google Book API
     * @param desiredIndex number used to return a specific Volume at that index
     */
    public void viewVolumeFromList(List<Volume> volumes, int desiredIndex) {
        // Get volume at desired index
        Volume volume = volumes.get(desiredIndex);

        // Gather the volume info
        Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();

        // ISBN
        for (Volume.VolumeInfo.IndustryIdentifiers ii : volumeInfo.getIndustryIdentifiers()) {
            if (ii.getType().equals("ISBN_13")) {
                System.out.println("ISBN     : " + ii.getIdentifier());
                break;
            } else if (ii.getType().equals("ISBN_10")) {
                System.out.println("ISBN     : " + ii.getIdentifier());
                break;
            } else {
                System.out.println("Other ID : " + ii.getIdentifier());
            }
        }

        // Title
        if (volumeInfo.getSubtitle() != null) {
            System.out.println(String.format("Title    : %s, %s",
                    volumeInfo.getTitle(), volumeInfo.getSubtitle()));
        } else {
            System.out.println(String.format("Title    : %s",
                    volumeInfo.getTitle()));
        }

        // Author(s)
        if (volumeInfo.getAuthors() != null) {
            System.out.println(String.format("Author(s): %s",
                    volumeInfo.getAuthors()));
        } else {
            System.out.println("Author(s): -");
        }

        // Genre(s)
        if (volumeInfo.getCategories() != null) {
            System.out.println(String.format("Genre(s) : %s",
                    volumeInfo.getCategories()));
        } else {
            System.out.println("Genre(s) : -");
        }

        // Thumbnail
        if (volumeInfo.getImageLinks() != null) {
            System.out.println(String.format("Image    : %s",
                    volumeInfo.getImageLinks().get("thumbnail")));
        }

        // Page count
        if (volumeInfo.getPageCount() != null) {
            System.out.println(String.format("Pages    : %s",
                    volumeInfo.getPageCount()));
        }
    }
}

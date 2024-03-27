package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.AddBookToBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.requests.AddSongToPlaylistRequest;
import com.nashss.se.musicplaylistservice.activity.results.AddBookToBooklistResult;
import com.nashss.se.musicplaylistservice.activity.results.AddSongToPlaylistResult;
import com.nashss.se.musicplaylistservice.dynamodb.AlbumTrackDao;
import com.nashss.se.musicplaylistservice.dynamodb.BookDao;
import com.nashss.se.musicplaylistservice.dynamodb.BooklistDao;
import com.nashss.se.musicplaylistservice.dynamodb.PlaylistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.AlbumTrack;
import com.nashss.se.musicplaylistservice.dynamodb.models.Book;
import com.nashss.se.musicplaylistservice.dynamodb.models.Booklist;
import com.nashss.se.musicplaylistservice.dynamodb.models.Playlist;
import com.nashss.se.musicplaylistservice.exceptions.AlbumTrackNotFoundException;
import com.nashss.se.musicplaylistservice.exceptions.BookNotFoundException;
import com.nashss.se.musicplaylistservice.exceptions.BooklistNotFoundException;
import com.nashss.se.musicplaylistservice.exceptions.PlaylistNotFoundException;
import com.nashss.se.musicplaylistservice.models.BookModel;
import com.nashss.se.musicplaylistservice.models.SongModel;
import com.nashss.se.musicplaylistservice.test.helper.AlbumTrackTestHelper;
import com.nashss.se.musicplaylistservice.test.helper.BookTestHelper;
import com.nashss.se.musicplaylistservice.test.helper.BooklistTestHelper;
import com.nashss.se.musicplaylistservice.test.helper.PlaylistTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class AddBookToBooklistActivityTest {
    @Mock
    private BooklistDao booklistDao;

    @Mock
    private BookDao bookDao;

    private AddBookToBooklistActivity addBookToBooklistActivity;

    @BeforeEach
    void setup() {
        openMocks(this);
        addBookToBooklistActivity = new AddBookToBooklistActivity(booklistDao, bookDao);
    }

    @Test
    void handleRequest_validRequest_addsBookToEndOfBooklist() {
        // GIVEN
        // a non-empty playlist
        Booklist originalBooklist = BooklistTestHelper.generateBooklist();
        String booklistId = originalBooklist.getId();
        String customerId = originalBooklist.getCustomerId();

        // the new song to add to the playlist
        Book bookToAdd = BookTestHelper.generateBook(2);
        String addedAsin = bookToAdd.getAsin();

        when(booklistDao.getBooklist(booklistId)).thenReturn(originalBooklist);
        when(booklistDao.saveBooklist(originalBooklist)).thenReturn(originalBooklist);
        when(bookDao.getBook(addedAsin)).thenReturn(bookToAdd);

        AddBookToBooklistRequest request = AddBookToBooklistRequest.builder()
                .withAsin(addedAsin)
                .withId(booklistId)
                .withCustomerId(customerId)
                .build();

        // WHEN
        AddBookToBooklistResult result = addBookToBooklistActivity.handleRequest(request);

        // THEN
        verify(booklistDao).saveBooklist(originalBooklist);

        assertEquals(2, result.getBookList().size());
        BookModel secondBook = result.getBookList().get(1);
        BookTestHelper.assertBookEqualsBookModel(bookToAdd, secondBook);
    }

    @Test
    public void handleRequest_noMatchingBooklistId_throwsBooklistNotFoundException() {
        // GIVEN
        String booklistId = "missing id";
        AddBookToBooklistRequest request = AddBookToBooklistRequest.builder()
                .withId(booklistId)
                .withAsin("asin")
                .withCustomerId("doesn't matter")
                .build();
        when(booklistDao.getBooklist(booklistId)).thenThrow(new BooklistNotFoundException());

        // WHEN + THEN
        assertThrows(BooklistNotFoundException.class, () -> addBookToBooklistActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_noMatchingBook_throwsBookNotFoundException() {
        // GIVEN
        Booklist booklist = BooklistTestHelper.generateBooklist();

        String booklistId = booklist.getId();
        String customerId = booklist.getCustomerId();
        String asin = "nonexistent asin";
        AddBookToBooklistRequest request = AddBookToBooklistRequest.builder()
                .withId(booklistId)
                .withAsin(asin)
                .withCustomerId(customerId)
                .build();

        // WHEN
        when(booklistDao.getBooklist(booklistId)).thenReturn(booklist);
        when(bookDao.getBook(asin)).thenThrow(new BookNotFoundException());

        // THEN
        assertThrows(BookNotFoundException.class, () -> addBookToBooklistActivity.handleRequest(request));
    }
}

//    @Test
//    void handleRequest_validRequestWithQueueNextFalse_addsSongToEndOfPlaylist() {
//        // GIVEN
//        int startingTrackCount = 3;
//        Playlist originalPlaylist = PlaylistTestHelper.generatePlaylistWithNAlbumTracks(startingTrackCount);
//        String playlistId = originalPlaylist.getId();
//        String customerId = originalPlaylist.getCustomerId();
//
//        // the new song to add to the playlist
//        AlbumTrack albumTrackToAdd = AlbumTrackTestHelper.generateAlbumTrack(8);
//        String addedAsin = albumTrackToAdd.getAsin();
//        int addedTracknumber = albumTrackToAdd.getTrackNumber();
//
//        when(playlistDao.getPlaylist(playlistId)).thenReturn(originalPlaylist);
//        when(playlistDao.savePlaylist(originalPlaylist)).thenReturn(originalPlaylist);
//        when(albumTrackDao.getAlbumTrack(addedAsin, addedTracknumber)).thenReturn(albumTrackToAdd);
//
//        AddSongToPlaylistRequest request = AddSongToPlaylistRequest.builder()
//                                               .withId(playlistId)
//                                               .withAsin(addedAsin)
//                                               .withTrackNumber(addedTracknumber)
//                                               .withQueueNext(false)
//                                               .withCustomerId(customerId)
//                                               .build();
//
//        // WHEN
//        AddSongToPlaylistResult result = addSongToPlaylistActivity.handleRequest(request);
//
//        // THEN
//        verify(playlistDao).savePlaylist(originalPlaylist);
//
//        assertEquals(startingTrackCount + 1, result.getSongList().size());
//        SongModel lastSong = result.getSongList().get(result.getSongList().size() - 1);
//        AlbumTrackTestHelper.assertAlbumTrackEqualsSongModel(albumTrackToAdd, lastSong);
//    }

//    @Test
//    void handleRequest_validRequestWithQueueNextTrue_addsSongToBeginningOfPlaylist() {
//        // GIVEN
//        int startingPlaylistSize = 2;
//        Playlist originalPlaylist = PlaylistTestHelper.generatePlaylistWithNAlbumTracks(startingPlaylistSize);
//        String playlistId = originalPlaylist.getId();
//        String customerId = originalPlaylist.getCustomerId();
//
//        // the new song to add to the playlist
//        AlbumTrack albumTrackToAdd = AlbumTrackTestHelper.generateAlbumTrack(6);
//        String addedAsin = albumTrackToAdd.getAsin();
//        int addedTracknumber = albumTrackToAdd.getTrackNumber();
//
//        when(playlistDao.getPlaylist(playlistId)).thenReturn(originalPlaylist);
//        when(playlistDao.savePlaylist(originalPlaylist)).thenReturn(originalPlaylist);
//        when(albumTrackDao.getAlbumTrack(addedAsin, addedTracknumber)).thenReturn(albumTrackToAdd);
//
//        AddSongToPlaylistRequest request = AddSongToPlaylistRequest.builder()
//                                               .withId(playlistId)
//                                               .withAsin(addedAsin)
//                                               .withTrackNumber(addedTracknumber)
//                                               .withQueueNext(true)
//                                               .withCustomerId(customerId)
//                                               .build();
//
//        // WHEN
//        AddSongToPlaylistResult result = addSongToPlaylistActivity.handleRequest(request);
//
//        // THEN
//        verify(playlistDao).savePlaylist(originalPlaylist);
//
//        assertEquals(startingPlaylistSize + 1, result.getSongList().size());
//        SongModel firstSong = result.getSongList().get(0);
//        AlbumTrackTestHelper.assertAlbumTrackEqualsSongModel(albumTrackToAdd, firstSong);
//    }
//}

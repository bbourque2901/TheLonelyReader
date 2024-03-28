package com.nashss.se.musicplaylistservice.dependency;

import com.nashss.se.musicplaylistservice.activity.*;

import dagger.Component;

import javax.inject.Singleton;

/**
 * Dagger component for providing dependency injection in the Music Playlist Service.
 */
@Singleton
@Component(modules = {DaoModule.class, MetricsModule.class})
public interface ServiceComponent {

    /**
     * Provides the relevant activity.
     * @return AddSongToPlaylistActivity
     */
    AddSongToPlaylistActivity provideAddSongToPlaylistActivity();

    /**
     * Provides the relevant activity.
     * @return CreatePlaylistActivity
     */
    CreatePlaylistActivity provideCreatePlaylistActivity();

    /**
     * Provides the relevant activity.
     * @return GetPlaylistActivity
     */
    GetPlaylistActivity provideGetPlaylistActivity();

    /**
     * Provides the relevant activity.
     * @return GetPlaylistActivity
     */
    SearchPlaylistsActivity provideSearchPlaylistsActivity();

    /**
     * Provides the relevant activity.
     * @return GetPlaylistSongsActivity
     */
    GetPlaylistSongsActivity provideGetPlaylistSongsActivity();

    /**
     * Provides the relevant activity.
     * @return UpdatePlaylistActivity
     */
    UpdatePlaylistActivity provideUpdatePlaylistActivity();

    /**
     * Provides the relevant activity.
     * @return CreateBooklistActivity
     */
    CreateBooklistActivity provideCreateBooklistActivity();

    /**
     *Provides the relevant activity.
     * @return GetBooklistActivity
     */
    GetBooklistActivity provideGetBooklistActivity();

    /**
     * Provides the relevant activity.
     * @return AddBookToBooklistActivity
     */
    AddBookToBooklistActivity provideAddBookToBooklistActivity();

    /**
     * Provides the relevant activity.
     * @return SearchBooklistActivity
     */
    SearchBooklistsActivity provideSearchBooklistsActivity();

    /**
     * Provides the relevant activity.
     * @return UpdateBooklistActivity
     */
    UpdateBooklistActivity provideUpdateBooklistActivity();


    GetCurrentlyReadingActivity provideGetCurrentlyReadingActivity();

    /**
     * Provides the relevant activity.
     * @return SearchBooksActivity
     */
    SearchBooksActivity provideSearchBooksActivity();

    /**
     * Provides the relevant activity.
     * @return RemoveBooklistActivity
     */
    RemoveBooklistActivity provideRemoveBooklistActivity();


}

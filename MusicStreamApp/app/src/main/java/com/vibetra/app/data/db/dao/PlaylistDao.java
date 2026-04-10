package com.vibetra.app.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vibetra.app.data.db.entity.PlaylistEntity;
import com.vibetra.app.data.db.entity.PlaylistTrackEntity;
import com.vibetra.app.data.model.PlaylistSummary;

import java.util.List;

@Dao
public interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPlaylist(PlaylistEntity playlist);

    @Query("SELECT * FROM playlists ORDER BY createdAt DESC")
    LiveData<List<PlaylistEntity>> getPlaylists();

        @Query("SELECT p.id AS id, p.name AS name, p.createdAt AS createdAt, " +
            "(SELECT COUNT(*) FROM playlist_tracks pt WHERE pt.playlistId = p.id) AS trackCount, " +
            "(SELECT pt2.artworkUrl FROM playlist_tracks pt2 " +
            "WHERE pt2.playlistId = p.id AND pt2.artworkUrl IS NOT NULL AND pt2.artworkUrl != '' LIMIT 1) AS firstArtworkUrl " +
            "FROM playlists p ORDER BY p.createdAt DESC")
        LiveData<List<PlaylistSummary>> getPlaylistSummaries();

    @Query("SELECT * FROM playlists ORDER BY createdAt DESC")
    List<PlaylistEntity> getPlaylistsNow();

    @Query("DELETE FROM playlists WHERE id = :playlistId")
    void deletePlaylist(long playlistId);

    @Query("DELETE FROM playlist_tracks WHERE playlistId = :playlistId")
    void deleteTracksByPlaylistId(long playlistId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTrackToPlaylist(PlaylistTrackEntity track);

    @Query("DELETE FROM playlist_tracks WHERE playlistId = :playlistId AND trackKey = :trackKey")
    void removeTrackFromPlaylist(long playlistId, String trackKey);

    @Query("SELECT * FROM playlist_tracks WHERE playlistId = :playlistId ORDER BY title COLLATE NOCASE ASC")
    LiveData<List<PlaylistTrackEntity>> getTracksForPlaylist(long playlistId);
}

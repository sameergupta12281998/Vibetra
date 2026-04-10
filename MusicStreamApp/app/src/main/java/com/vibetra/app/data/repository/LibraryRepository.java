package com.vibetra.app.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.vibetra.app.data.db.AppDatabase;
import com.vibetra.app.data.db.dao.FavoritesDao;
import com.vibetra.app.data.db.dao.PlaylistDao;
import com.vibetra.app.data.db.entity.FavoriteTrackEntity;
import com.vibetra.app.data.db.entity.PlaylistEntity;
import com.vibetra.app.data.db.entity.PlaylistTrackEntity;
import com.vibetra.app.data.model.PlaylistSummary;
import com.vibetra.app.data.model.Track;
import com.vibetra.app.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;

public class LibraryRepository {

    private final FavoritesDao favoritesDao;
    private final PlaylistDao playlistDao;

    public LibraryRepository(AppDatabase database) {
        this.favoritesDao = database.favoritesDao();
        this.playlistDao = database.playlistDao();
    }

    public LiveData<List<Track>> getFavorites() {
        return Transformations.map(favoritesDao.getAll(), entities -> {
            List<Track> tracks = new ArrayList<>();
            for (FavoriteTrackEntity entity : entities) {
                tracks.add(TrackMapper.fromFavoriteEntity(entity));
            }
            return tracks;
        });
    }

    public void addFavorite(Track track) {
        AppExecutors.io().execute(() -> favoritesDao.insert(TrackMapper.toFavoriteEntity(track)));
    }

    public void removeFavorite(Track track) {
        AppExecutors.io().execute(() -> favoritesDao.deleteByTrackKey(TrackMapper.trackKey(track)));
    }

    public void toggleFavorite(Track track, RepositoryCallback<Boolean> callback) {
        AppExecutors.io().execute(() -> {
            boolean isFavorite = favoritesDao.isFavorite(TrackMapper.trackKey(track));
            if (isFavorite) {
                favoritesDao.deleteByTrackKey(TrackMapper.trackKey(track));
            } else {
                favoritesDao.insert(TrackMapper.toFavoriteEntity(track));
            }
            callback.onSuccess(!isFavorite);
        });
    }

    public LiveData<List<PlaylistEntity>> getPlaylists() {
        return playlistDao.getPlaylists();
    }

    public LiveData<List<PlaylistSummary>> getPlaylistSummaries() {
        return playlistDao.getPlaylistSummaries();
    }

    public void fetchPlaylistsOnce(RepositoryCallback<List<PlaylistEntity>> callback) {
        AppExecutors.io().execute(() -> callback.onSuccess(playlistDao.getPlaylistsNow()));
    }

    public void createPlaylist(String name) {
        AppExecutors.io().execute(() -> {
            PlaylistEntity entity = new PlaylistEntity();
            entity.setName(name);
            entity.setCreatedAt(System.currentTimeMillis());
            playlistDao.insertPlaylist(entity);
        });
    }

    public void deletePlaylist(long playlistId) {
        AppExecutors.io().execute(() -> {
            playlistDao.deleteTracksByPlaylistId(playlistId);
            playlistDao.deletePlaylist(playlistId);
        });
    }

    public LiveData<List<Track>> getTracksForPlaylist(long playlistId) {
        return Transformations.map(playlistDao.getTracksForPlaylist(playlistId), entities -> {
            List<Track> tracks = new ArrayList<>();
            for (PlaylistTrackEntity entity : entities) {
                tracks.add(TrackMapper.fromPlaylistTrackEntity(entity));
            }
            return tracks;
        });
    }

    public void addTrackToPlaylist(long playlistId, Track track) {
        AppExecutors.io().execute(() -> playlistDao.addTrackToPlaylist(TrackMapper.toPlaylistTrack(playlistId, track)));
    }

    public void removeTrackFromPlaylist(long playlistId, Track track) {
        AppExecutors.io().execute(() -> playlistDao.removeTrackFromPlaylist(playlistId, TrackMapper.trackKey(track)));
    }
}

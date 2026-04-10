package com.vibetra.app.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.vibetra.app.data.db.AppDatabase;
import com.vibetra.app.data.model.PlaylistSummary;
import com.vibetra.app.data.model.Track;
import com.vibetra.app.data.repository.LibraryRepository;

import java.util.ArrayList;
import java.util.List;

public class PlaylistViewModel extends AndroidViewModel {

    private final LibraryRepository libraryRepository;
    private final LiveData<List<PlaylistSummary>> playlists;
    private final MutableLiveData<Long> selectedPlaylistId = new MutableLiveData<>(-1L);
    private final LiveData<List<Track>> selectedPlaylistTracks;

    public PlaylistViewModel(@NonNull Application application) {
        super(application);
        libraryRepository = new LibraryRepository(AppDatabase.getInstance(application));
        playlists = libraryRepository.getPlaylistSummaries();
        selectedPlaylistTracks = Transformations.switchMap(selectedPlaylistId, id -> {
            if (id == null || id <= 0) {
                MutableLiveData<List<Track>> empty = new MutableLiveData<>();
                empty.setValue(new ArrayList<>());
                return empty;
            }
            return libraryRepository.getTracksForPlaylist(id);
        });
    }

    public LiveData<List<PlaylistSummary>> getPlaylists() {
        return playlists;
    }

    public LiveData<List<Track>> getSelectedPlaylistTracks() {
        return selectedPlaylistTracks;
    }

    public LiveData<Long> getSelectedPlaylistId() {
        return selectedPlaylistId;
    }

    public void selectPlaylist(long playlistId) {
        selectedPlaylistId.setValue(playlistId);
    }

    public void createPlaylist(String name) {
        if (!TextUtils.isEmpty(name)) {
            libraryRepository.createPlaylist(name.trim());
        }
    }

    public void deletePlaylist(long playlistId) {
        if (playlistId > 0) {
            libraryRepository.deletePlaylist(playlistId);
            Long selected = selectedPlaylistId.getValue();
            if (selected != null && selected == playlistId) {
                selectedPlaylistId.setValue(-1L);
            }
        }
    }

    public void addTrackToSelectedPlaylist(Track track) {
        Long playlistId = selectedPlaylistId.getValue();
        if (playlistId != null && playlistId > 0) {
            libraryRepository.addTrackToPlaylist(playlistId, track);
        }
    }

    public void removeTrackFromSelectedPlaylist(Track track) {
        Long playlistId = selectedPlaylistId.getValue();
        if (playlistId != null && playlistId > 0) {
            libraryRepository.removeTrackFromPlaylist(playlistId, track);
        }
    }
}

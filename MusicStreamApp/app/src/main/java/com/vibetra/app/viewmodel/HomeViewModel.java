package com.vibetra.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vibetra.app.data.model.Track;
import com.vibetra.app.data.repository.MusicRepository;
import com.vibetra.app.utils.Result;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MusicRepository musicRepository = new MusicRepository();

    private final MutableLiveData<Result<List<Track>>> trending = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Track>>> popular = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Track>>> albums = new MutableLiveData<>();

    public LiveData<Result<List<Track>>> getTrending() {
        return trending;
    }

    public LiveData<Result<List<Track>>> getPopular() {
        return popular;
    }

    public LiveData<Result<List<Track>>> getAlbums() {
        return albums;
    }

    public void loadHomeData() {
        trending.setValue(Result.loading());
        popular.setValue(Result.loading());
        albums.setValue(Result.loading());

        musicRepository.getTrendingTracks(20, new com.vibetra.app.data.repository.RepositoryCallback<List<Track>>() {
            @Override
            public void onSuccess(List<Track> result) {
                trending.postValue(Result.success(result));
            }

            @Override
            public void onError(String message) {
                trending.postValue(Result.error(message));
            }
        });

        musicRepository.getPopularTracks(20, new com.vibetra.app.data.repository.RepositoryCallback<List<Track>>() {
            @Override
            public void onSuccess(List<Track> result) {
                popular.postValue(Result.success(result));
            }

            @Override
            public void onError(String message) {
                popular.postValue(Result.error(message));
            }
        });

        musicRepository.getAlbums(20, new com.vibetra.app.data.repository.RepositoryCallback<List<Track>>() {
            @Override
            public void onSuccess(List<Track> result) {
                albums.postValue(Result.success(result));
            }

            @Override
            public void onError(String message) {
                albums.postValue(Result.error(message));
            }
        });
    }
}

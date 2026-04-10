package com.vibetra.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vibetra.app.data.model.Track;
import com.vibetra.app.data.repository.MusicRepository;
import com.vibetra.app.data.repository.RepositoryCallback;
import com.vibetra.app.utils.Result;

import java.util.List;

public class SearchViewModel extends ViewModel {

    private final MusicRepository musicRepository = new MusicRepository();
    private final MutableLiveData<Result<List<Track>>> searchResult = new MutableLiveData<>();

    public LiveData<Result<List<Track>>> getSearchResult() {
        return searchResult;
    }

    public void search(String query) {
        searchResult.setValue(Result.loading());
        musicRepository.searchTracks(query, 30, new RepositoryCallback<List<Track>>() {
            @Override
            public void onSuccess(List<Track> result) {
                searchResult.postValue(Result.success(result));
            }

            @Override
            public void onError(String message) {
                searchResult.postValue(Result.error(message));
            }
        });
    }
}

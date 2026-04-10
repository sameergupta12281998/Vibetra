package com.vibetra.app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vibetra.app.data.db.AppDatabase;
import com.vibetra.app.data.model.Track;
import com.vibetra.app.data.repository.LibraryRepository;
import com.vibetra.app.data.repository.RepositoryCallback;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {

    private final LibraryRepository libraryRepository;
    private final LiveData<List<Track>> favorites;
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        libraryRepository = new LibraryRepository(AppDatabase.getInstance(application));
        favorites = libraryRepository.getFavorites();
    }

    public LiveData<List<Track>> getFavorites() {
        return favorites;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    public void toggleFavorite(Track track) {
        libraryRepository.toggleFavorite(track, new RepositoryCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                toastMessage.postValue(result ? "Added to favorites" : "Removed from favorites");
            }

            @Override
            public void onError(String message) {
                toastMessage.postValue(message);
            }
        });
    }
}

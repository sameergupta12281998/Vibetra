package com.vibetra.app.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.vibetra.app.data.db.AppDatabase;
import com.vibetra.app.data.db.entity.PlaylistEntity;
import com.vibetra.app.data.model.Track;
import com.vibetra.app.data.repository.LibraryRepository;
import com.vibetra.app.data.repository.RepositoryCallback;

import java.util.ArrayList;
import java.util.List;

public final class PlaylistDialogHelper {

    private PlaylistDialogHelper() {
    }

    public static void showAddToPlaylistDialog(Context context, Track track) {
        LibraryRepository repository = new LibraryRepository(AppDatabase.getInstance(context));
        Handler mainHandler = new Handler(Looper.getMainLooper());
        repository.fetchPlaylistsOnce(new RepositoryCallback<List<PlaylistEntity>>() {
            @Override
            public void onSuccess(List<PlaylistEntity> result) {
                mainHandler.post(() -> {
                    if (result == null || result.isEmpty()) {
                        Toast.makeText(context, "Create a playlist first", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<String> names = new ArrayList<>();
                    for (PlaylistEntity entity : result) {
                        names.add(entity.getName());
                    }
                    new AlertDialog.Builder(context)
                            .setTitle("Add to playlist")
                            .setItems(names.toArray(new String[0]), (dialog, which) -> {
                                PlaylistEntity selected = result.get(which);
                                repository.addTrackToPlaylist(selected.getId(), track);
                                Toast.makeText(context, "Added to " + selected.getName(), Toast.LENGTH_SHORT).show();
                            })
                            .show();
                });
            }

            @Override
            public void onError(String message) {
                mainHandler.post(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
            }
        });
    }
}

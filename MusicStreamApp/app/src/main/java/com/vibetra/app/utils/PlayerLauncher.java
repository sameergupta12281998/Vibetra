package com.vibetra.app.utils;

import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.vibetra.app.data.model.Track;
import com.vibetra.app.player.MusicPlaybackService;
import com.vibetra.app.ui.player.PlayerActivity;

import java.util.ArrayList;
import java.util.List;

public final class PlayerLauncher {

    private PlayerLauncher() {
    }

    public static void playTrackList(Context context, List<Track> tracks, int index, boolean hdEnabled) {
        ArrayList<Track> serializableTracks = new ArrayList<>(tracks);

        Intent serviceIntent = new Intent(context, MusicPlaybackService.class);
        serviceIntent.setAction(Constants.ACTION_PLAY_TRACKS);
        serviceIntent.putExtra(Constants.EXTRA_TRACK_LIST, serializableTracks);
        serviceIntent.putExtra(Constants.EXTRA_TRACK_INDEX, index);
        serviceIntent.putExtra(Constants.EXTRA_IS_HD, hdEnabled);
        ContextCompat.startForegroundService(context, serviceIntent);

        Intent playerIntent = new Intent(context, PlayerActivity.class);
        playerIntent.putExtra(Constants.EXTRA_TRACK_LIST, serializableTracks);
        playerIntent.putExtra(Constants.EXTRA_TRACK_INDEX, index);
        playerIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(playerIntent);
    }
}

package com.vibetra.app.player;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vibetra.app.data.model.Track;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayerManager {

    private static volatile MusicPlayerManager INSTANCE;

    private final ExoPlayer player;
    private final List<Track> queue = new ArrayList<>();
    private final MutableLiveData<Track> nowPlaying = new MutableLiveData<>();
    private final MutableLiveData<Boolean> playing = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> shuffleEnabled = new MutableLiveData<>(false);
    private int retryCount = 0;

    private MusicPlayerManager(Context context) {
        player = new ExoPlayer.Builder(context.getApplicationContext()).build();
        player.setRepeatMode(Player.REPEAT_MODE_OFF);
        player.setShuffleModeEnabled(false);
        player.addListener(new Player.Listener() {
            @Override
            public void onMediaItemTransition(MediaItem mediaItem, int reason) {
                int index = player.getCurrentMediaItemIndex();
                if (index >= 0 && index < queue.size()) {
                    nowPlaying.postValue(queue.get(index));
                }
            }

            @Override
            public void onPlayerError(com.google.android.exoplayer2.PlaybackException error) {
                if (retryCount < 2) {
                    retryCount++;
                    player.prepare();
                    player.play();
                }
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_READY) {
                    retryCount = 0;
                }
                if (playbackState == Player.STATE_ENDED) {
                    playing.postValue(false);
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                playing.postValue(isPlaying);
            }
        });
    }

    public static MusicPlayerManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MusicPlayerManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MusicPlayerManager(context);
                }
            }
        }
        return INSTANCE;
    }

    public void setQueue(List<Track> tracks, int startIndex, boolean hdRequested) {
        queue.clear();
        if (tracks != null) {
            for (Track track : tracks) {
                if (track.getStreamUrl() != null && !track.getStreamUrl().isEmpty()) {
                    queue.add(track);
                }
            }
        }
        List<MediaItem> mediaItems = new ArrayList<>();
        for (Track track : queue) {
            mediaItems.add(MediaItem.fromUri(Uri.parse(track.getStreamUrl())));
        }
        if (mediaItems.isEmpty()) {
            nowPlaying.postValue(null);
            playing.postValue(false);
            return;
        }
        int safeIndex = Math.max(0, Math.min(startIndex, mediaItems.size() - 1));
        player.setMediaItems(mediaItems, safeIndex, 0);
        player.prepare();
        player.play();
        if (!queue.isEmpty()) {
            nowPlaying.postValue(queue.get(safeIndex));
        }
    }

    public void play() {
        player.play();
    }

    public void pause() {
        player.pause();
    }

    public void seekTo(long positionMs) {
        player.seekTo(positionMs);
    }

    public void next() {
        player.seekToNext();
    }

    public void previous() {
        player.seekToPrevious();
    }

    public ExoPlayer getPlayer() {
        return player;
    }

    public LiveData<Track> getNowPlaying() {
        return nowPlaying;
    }

    public LiveData<Boolean> getPlaying() {
        return playing;
    }

    public LiveData<Boolean> getShuffleEnabled() {
        return shuffleEnabled;
    }

    public void setShuffleEnabled(boolean enabled) {
        player.setShuffleModeEnabled(enabled);
        shuffleEnabled.postValue(enabled);
    }

    public void toggleShuffleMode() {
        boolean enabled = !player.getShuffleModeEnabled();
        player.setShuffleModeEnabled(enabled);
        shuffleEnabled.postValue(enabled);
    }

    public boolean hasQueue() {
        return !queue.isEmpty();
    }

    public List<Track> getQueue() {
        return new ArrayList<>(queue);
    }
}

package com.vibetra.app.player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.Nullable;

import com.vibetra.app.R;
import com.vibetra.app.data.model.Track;
import com.vibetra.app.ui.player.PlayerActivity;
import com.vibetra.app.utils.Constants;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import java.util.ArrayList;

public class MusicPlaybackService extends Service {

    private MusicPlayerManager playerManager;
    private PlayerNotificationManager notificationManager;
    private MediaSessionCompat mediaSession;
    private MediaSessionConnector mediaSessionConnector;

    @Override
    public void onCreate() {
        super.onCreate();
        createChannel();

        playerManager = MusicPlayerManager.getInstance(this);
        setupMediaSession();
        setupNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && Constants.ACTION_PLAY_TRACKS.equals(intent.getAction())) {
            ArrayList<Track> list = (ArrayList<Track>) intent.getSerializableExtra(Constants.EXTRA_TRACK_LIST);
            int index = intent.getIntExtra(Constants.EXTRA_TRACK_INDEX, 0);
            boolean hd = intent.getBooleanExtra(Constants.EXTRA_IS_HD, false);
            if (list != null && !list.isEmpty()) {
                playerManager.setQueue(list, index, hd);
            }
        }
        return START_STICKY;
    }

    private void setupNotification() {
        Player player = playerManager.getPlayer();

        PlayerNotificationManager.MediaDescriptionAdapter mediaDescriptionAdapter = new PlayerNotificationManager.MediaDescriptionAdapter() {
            @Override
            public CharSequence getCurrentContentTitle(Player player) {
                Track track = playerManager.getNowPlaying().getValue();
                return track != null ? track.getTitle() : getString(R.string.app_name);
            }

            @Nullable
            @Override
            public PendingIntent createCurrentContentIntent(Player player) {
                Intent intent = new Intent(MusicPlaybackService.this, PlayerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                int flag = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                        : PendingIntent.FLAG_UPDATE_CURRENT;
                return PendingIntent.getActivity(MusicPlaybackService.this, 10, intent, flag);
            }

            @Nullable
            @Override
            public CharSequence getCurrentContentText(Player player) {
                Track track = playerManager.getNowPlaying().getValue();
                return track != null ? track.getArtistName() : "";
            }

            @Override
            public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                return null;
            }
        };

        notificationManager = new PlayerNotificationManager.Builder(this, Constants.NOTIFICATION_ID, Constants.NOTIFICATION_CHANNEL_ID)
                .setMediaDescriptionAdapter(mediaDescriptionAdapter)
                .setNotificationListener(new PlayerNotificationManager.NotificationListener() {
                    @Override
                    public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
                        startForeground(notificationId, notification);
                    }

                    @Override
                    public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
                        stopForeground(STOP_FOREGROUND_REMOVE);
                        stopSelf();
                    }
                })
                .setSmallIconResourceId(R.drawable.ic_music_note)
                .build();

        notificationManager.setMediaSessionToken(mediaSession.getSessionToken());
        notificationManager.setUseNextAction(true);
        notificationManager.setUsePreviousAction(true);
        notificationManager.setUsePlayPauseActions(true);
        notificationManager.setPlayer(player);
    }

    private void setupMediaSession() {
        Intent sessionIntent = new Intent(this, PlayerActivity.class);
        sessionIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                : PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent sessionActivity = PendingIntent.getActivity(this, 11, sessionIntent, flags);

        mediaSession = new MediaSessionCompat(this, getString(R.string.app_name));
        mediaSession.setActive(true);
        mediaSession.setSessionActivity(sessionActivity);

        mediaSessionConnector = new MediaSessionConnector(mediaSession);
        mediaSessionConnector.setPlayer(playerManager.getPlayer());
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    Constants.NOTIFICATION_CHANNEL_ID,
                    getString(R.string.playback_channel_name),
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (notificationManager != null) {
            notificationManager.setPlayer(null);
        }
        if (mediaSessionConnector != null) {
            mediaSessionConnector.setPlayer(null);
        }
        if (mediaSession != null) {
            mediaSession.setActive(false);
            mediaSession.release();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

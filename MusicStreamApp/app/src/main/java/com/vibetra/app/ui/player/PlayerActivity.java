package com.vibetra.app.ui.player;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.vibetra.app.R;
import com.vibetra.app.data.model.Track;
import com.vibetra.app.player.MusicPlayerManager;
import com.vibetra.app.utils.AdManager;
import com.vibetra.app.utils.Constants;
import com.vibetra.app.utils.TimeUtils;
import com.google.android.exoplayer2.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends AppCompatActivity {

    private final Handler handler = new Handler(Looper.getMainLooper());

    private MusicPlayerManager playerManager;
    private AdManager adManager;

    private TextView tvTitle;
    private TextView tvArtist;
    private TextView tvCurrent;
    private TextView tvDuration;
    private TextView tvPlayMode;
    private ImageView ivArtwork;
    private SeekBar seekBar;
    private ImageButton btnPlayPause;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        playerManager = MusicPlayerManager.getInstance(this);
        adManager = new AdManager(this);

        tvTitle = findViewById(R.id.tvPlayerTitle);
        tvArtist = findViewById(R.id.tvPlayerArtist);
        tvCurrent = findViewById(R.id.tvCurrentTime);
        tvDuration = findViewById(R.id.tvTotalTime);
        tvPlayMode = findViewById(R.id.tvPlayMode);
        ivArtwork = findViewById(R.id.ivPlayerArtwork);
        seekBar = findViewById(R.id.seekPlayer);
        btnPlayPause = findViewById(R.id.btnPlayPause);

        ImageButton btnPrev = findViewById(R.id.btnPrev);
        ImageButton btnNext = findViewById(R.id.btnNext);
        Button btnHd = findViewById(R.id.btnRewardHd);

        btnPlayPause.setOnClickListener(v -> {
            if (playerManager.getPlayer().isPlaying()) {
                playerManager.pause();
            } else {
                playerManager.play();
            }
            updatePlayButton();
        });

        btnPrev.setOnClickListener(v -> playerManager.previous());
        btnNext.setOnClickListener(v -> playerManager.next());
        tvPlayMode.setOnClickListener(v -> {
            playerManager.toggleShuffleMode();
            updatePlayModeLabel();
        });

        btnHd.setOnClickListener(v -> adManager.showRewarded(this, new AdManager.RewardCallback() {
            @Override
            public void onRewarded() {
                Toast.makeText(PlayerActivity.this, R.string.hd_unlocked, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClosed() {
            }
        }));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    playerManager.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        playerManager.getNowPlaying().observe(this, this::bindTrack);
        playerManager.getShuffleEnabled().observe(this, enabled -> updatePlayModeLabel());

        playerManager.getPlayer().addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                updatePlayButton();
                if (playbackState == Player.STATE_BUFFERING) {
                    tvCurrent.setText(R.string.buffering);
                }
            }
        });

        handleIntentQueue();
        updatePlayModeLabel();
        startProgressLoop();
    }

    private void handleIntentQueue() {
        ArrayList<Track> tracks = (ArrayList<Track>) getIntent().getSerializableExtra(Constants.EXTRA_TRACK_LIST);
        int index = getIntent().getIntExtra(Constants.EXTRA_TRACK_INDEX, 0);
        if (tracks != null && !tracks.isEmpty()) {
            playerManager.setQueue(tracks, index, adManager.isHdUnlocked());
            bindTrack(tracks.get(Math.max(0, Math.min(index, tracks.size() - 1))));
        } else {
            List<Track> queue = playerManager.getQueue();
            if (!queue.isEmpty()) {
                int currentIndex = playerManager.getPlayer().getCurrentMediaItemIndex();
                if (currentIndex < 0 || currentIndex >= queue.size()) {
                    currentIndex = 0;
                }
                bindTrack(queue.get(currentIndex));
            }
        }
    }

    private void bindTrack(Track track) {
        if (track == null) {
            return;
        }
        tvTitle.setText(track.getTitle());
        tvArtist.setText(track.getArtistName());
        tvDuration.setText(TimeUtils.formatDuration(track.getDurationSec()));

        Glide.with(this)
                .load(track.getArtworkUrl())
                .placeholder(R.drawable.ic_music_note)
                .into(ivArtwork);
    }

    private void startProgressLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    long current = playerManager.getPlayer().getCurrentPosition();
                    long duration = Math.max(playerManager.getPlayer().getDuration(), 0);
                    seekBar.setMax((int) duration);
                    seekBar.setProgress((int) current);
                    tvCurrent.setText(TimeUtils.formatMillis(current));
                    if (duration > 0) {
                        tvDuration.setText(TimeUtils.formatMillis(duration));
                    }
                    updatePlayButton();
                    handler.postDelayed(this, 1000L);
                }
            }
        }, 300L);
    }

    private void updatePlayButton() {
        btnPlayPause.setImageResource(playerManager.getPlayer().isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    private void updatePlayModeLabel() {
        boolean shuffle = playerManager.getPlayer().getShuffleModeEnabled();
        tvPlayMode.setText(shuffle ? R.string.play_mode_shuffle : R.string.play_mode_sequence);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}

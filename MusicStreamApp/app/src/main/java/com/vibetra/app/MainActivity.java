package com.vibetra.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.vibetra.app.data.model.Track;
import com.vibetra.app.player.MusicPlayerManager;
import com.vibetra.app.ui.favorites.FavoritesFragment;
import com.vibetra.app.ui.home.HomeFragment;
import com.vibetra.app.ui.playlist.PlaylistFragment;
import com.vibetra.app.ui.player.PlayerActivity;
import com.vibetra.app.ui.search.SearchFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_POST_NOTIFICATIONS = 2001;
    private static final String PRIVACY_POLICY_URL = BuildConfig.PRIVACY_POLICY_URL;

    private MusicPlayerManager playerManager;
    private View miniPlayer;
    private ImageView miniArtwork;
    private TextView miniTitle;
    private TextView miniArtist;
    private ImageButton miniPlayPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ensureNotificationPermission();

        playerManager = MusicPlayerManager.getInstance(this);
        setupMiniPlayer();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                switchFragment(new HomeFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_search) {
                switchFragment(new SearchFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_favorites) {
                switchFragment(new FavoritesFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_playlist) {
                switchFragment(new PlaylistFragment());
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }

        playerManager.getNowPlaying().observe(this, this::bindMiniPlayer);
        playerManager.getPlaying().observe(this, isPlaying -> updateMiniPlayerButton(Boolean.TRUE.equals(isPlaying)));
    }

    private void setupMiniPlayer() {
        miniPlayer = findViewById(R.id.miniPlayer);
        miniArtwork = findViewById(R.id.ivMiniArtwork);
        miniTitle = findViewById(R.id.tvMiniTitle);
        miniArtist = findViewById(R.id.tvMiniArtist);
        miniPlayPause = findViewById(R.id.btnMiniPlayPause);

        miniPlayer.setOnClickListener(v -> openPlayer());
        miniPlayPause.setOnClickListener(v -> {
            if (playerManager.getPlayer().isPlaying()) {
                playerManager.pause();
            } else {
                playerManager.play();
            }
            updateMiniPlayerButton(playerManager.getPlayer().isPlaying());
        });
    }

    private void bindMiniPlayer(Track track) {
        if (track == null || !playerManager.hasQueue()) {
            miniPlayer.setVisibility(View.GONE);
            return;
        }

        miniPlayer.setVisibility(View.VISIBLE);
        miniTitle.setText(track.getTitle());
        miniArtist.setText(track.getArtistName());
        Glide.with(this)
                .load(track.getArtworkUrl())
                .placeholder(R.drawable.ic_music_note)
                .into(miniArtwork);
        updateMiniPlayerButton(playerManager.getPlayer().isPlaying());
    }

    private void updateMiniPlayerButton(boolean isPlaying) {
        miniPlayPause.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    private void openPlayer() {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void ensureNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                REQUEST_POST_NOTIFICATIONS);
    }

    private void switchFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_privacy_policy) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL)));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

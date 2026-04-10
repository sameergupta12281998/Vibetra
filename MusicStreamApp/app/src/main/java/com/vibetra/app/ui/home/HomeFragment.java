package com.vibetra.app.ui.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.vibetra.app.BuildConfig;
import com.vibetra.app.R;
import com.vibetra.app.adapters.TrackAdapter;
import com.vibetra.app.data.model.Track;
import com.vibetra.app.utils.AdManager;
import com.vibetra.app.utils.NetworkUtils;
import com.vibetra.app.utils.PlayerLauncher;
import com.vibetra.app.utils.PlaylistDialogHelper;
import com.vibetra.app.utils.Result;
import com.vibetra.app.viewmodel.FavoritesViewModel;
import com.vibetra.app.viewmodel.HomeViewModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import android.util.Log;

import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FavoritesViewModel favoritesViewModel;
    private AdManager adManager;

    private TrackAdapter trendingAdapter;
    private TrackAdapter popularAdapter;
    private TrackAdapter albumAdapter;

    private ProgressBar progress;
    private SwipeRefreshLayout swipeRefresh;
    private TextView errorText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        favoritesViewModel = new ViewModelProvider(requireActivity()).get(FavoritesViewModel.class);
        adManager = new AdManager(requireContext());

        progress = view.findViewById(R.id.progressHome);
        swipeRefresh = view.findViewById(R.id.swipeRefreshHome);
        errorText = view.findViewById(R.id.tvHomeError);

        setupRecyclerViews(view);
        setupBanner(view);

        swipeRefresh.setOnRefreshListener(this::loadData);

        observeData();
        loadData();
    }

    private void setupRecyclerViews(View view) {
        RecyclerView rvTrending = view.findViewById(R.id.rvTrending);
        RecyclerView rvPopular = view.findViewById(R.id.rvPopular);
        RecyclerView rvAlbums = view.findViewById(R.id.rvAlbums);

        trendingAdapter = new TrackAdapter(new TrackAdapter.Listener() {
            @Override
            public void onPlay(Track track, int position) {
                List<Track> tracks = trendingAdapter.getItems();
                playFromList(track, tracks, position);
            }

            @Override
            public void onFavorite(Track track) {
                favoritesViewModel.toggleFavorite(track);
            }

            @Override
            public void onAddToPlaylist(Track track) {
                PlaylistDialogHelper.showAddToPlaylistDialog(requireContext(), track);
            }
        }, true, true);

        popularAdapter = new TrackAdapter(new TrackAdapter.Listener() {
            @Override
            public void onPlay(Track track, int position) {
                List<Track> tracks = popularAdapter.getItems();
                playFromList(track, tracks, position);
            }

            @Override
            public void onFavorite(Track track) {
                favoritesViewModel.toggleFavorite(track);
            }

            @Override
            public void onAddToPlaylist(Track track) {
                PlaylistDialogHelper.showAddToPlaylistDialog(requireContext(), track);
            }
        }, true, true);

        albumAdapter = new TrackAdapter(new TrackAdapter.Listener() {
            @Override
            public void onPlay(Track track, int position) {
                List<Track> tracks = albumAdapter.getItems();
                playFromList(track, tracks, position);
            }

            @Override
            public void onFavorite(Track track) {
                favoritesViewModel.toggleFavorite(track);
            }

            @Override
            public void onAddToPlaylist(Track track) {
                PlaylistDialogHelper.showAddToPlaylistDialog(requireContext(), track);
            }
        }, true, true);

        rvTrending.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        rvPopular.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        rvAlbums.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));

        rvTrending.setAdapter(trendingAdapter);
        rvPopular.setAdapter(popularAdapter);
        rvAlbums.setAdapter(albumAdapter);
    }

    private void playFromList(Track track, List<Track> tracks, int position) {
        if (track.getStreamUrl() == null || track.getStreamUrl().isEmpty()) {
            Toast.makeText(requireContext(), R.string.youtube_fallback_note, Toast.LENGTH_SHORT).show();
            return;
        }
        adManager.incrementPlayCount();
        if (adManager.shouldShowInterstitial()) {
            adManager.showInterstitialIfReady(requireActivity());
        }
        PlayerLauncher.playTrackList(requireContext(), tracks, position, adManager.isHdUnlocked());
    }

    private void setupBanner(View view) {
        AdView adView = view.findViewById(R.id.adViewHome);
        if (TextUtils.isEmpty(BuildConfig.ADMOB_BANNER_UNIT_ID)) {
            adView.setVisibility(View.GONE);
            return;
        }
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (BuildConfig.DEBUG) Log.d("ADS_HOME", "Banner loaded successfully");
            }
            @Override
            public void onAdFailedToLoad(LoadAdError error) {
                if (BuildConfig.DEBUG) Log.e("ADS_HOME", "Banner failed: " + error.getCode() + " - " + error.getMessage());
            }
        });
        adView.loadAd(new AdRequest.Builder().build());
    }

    private void observeData() {
        homeViewModel.getTrending().observe(getViewLifecycleOwner(), result -> bindResult(result, trendingAdapter));
        homeViewModel.getPopular().observe(getViewLifecycleOwner(), result -> bindResult(result, popularAdapter));
        homeViewModel.getAlbums().observe(getViewLifecycleOwner(), result -> bindResult(result, albumAdapter));
        favoritesViewModel.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindResult(Result<List<Track>> result, TrackAdapter adapter) {
        if (result == null) {
            return;
        }
        if (result.getStatus() == Result.Status.LOADING) {
            progress.setVisibility(View.VISIBLE);
            errorText.setVisibility(View.GONE);
        } else if (result.getStatus() == Result.Status.SUCCESS) {
            progress.setVisibility(View.GONE);
            swipeRefresh.setRefreshing(false);
            adapter.submitList(result.getData());
        } else {
            progress.setVisibility(View.GONE);
            swipeRefresh.setRefreshing(false);
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(result.getError());
        }
    }

    private void loadData() {
        if (!NetworkUtils.isOnline(requireContext())) {
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(R.string.error_no_internet);
            swipeRefresh.setRefreshing(false);
            trendingAdapter.submitList(Collections.emptyList());
            popularAdapter.submitList(Collections.emptyList());
            albumAdapter.submitList(Collections.emptyList());
            return;
        }
        homeViewModel.loadHomeData();
    }
}

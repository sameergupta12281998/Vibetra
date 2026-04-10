package com.vibetra.app.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vibetra.app.BuildConfig;
import com.vibetra.app.R;
import com.vibetra.app.adapters.TrackAdapter;
import com.vibetra.app.data.model.Track;
import com.vibetra.app.utils.AdManager;
import com.vibetra.app.utils.PlayerLauncher;
import com.vibetra.app.utils.PlaylistDialogHelper;
import com.vibetra.app.viewmodel.FavoritesViewModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import android.util.Log;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private FavoritesViewModel favoritesViewModel;
    private TrackAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        favoritesViewModel = new ViewModelProvider(requireActivity()).get(FavoritesViewModel.class);
        AdManager adManager = new AdManager(requireContext());

        RecyclerView recyclerView = view.findViewById(R.id.rvFavorites);
        TextView emptyView = view.findViewById(R.id.tvFavoritesEmpty);

        adapter = new TrackAdapter(new TrackAdapter.Listener() {
            @Override
            public void onPlay(Track track, int position) {
                List<Track> tracks = adapter.getItems();
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

            @Override
            public void onFavorite(Track track) {
                favoritesViewModel.toggleFavorite(track);
            }

            @Override
            public void onAddToPlaylist(Track track) {
                PlaylistDialogHelper.showAddToPlaylistDialog(requireContext(), track);
            }
        }, true, true, true);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        setupBanner(view);

        favoritesViewModel.getFavorites().observe(getViewLifecycleOwner(), tracks -> {
            adapter.submitList(tracks);
            emptyView.setVisibility(tracks == null || tracks.isEmpty() ? View.VISIBLE : View.GONE);
        });

        favoritesViewModel.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBanner(View view) {
        AdView adView = view.findViewById(R.id.adViewFavorites);
        if (BuildConfig.ADMOB_BANNER_UNIT_ID == null || BuildConfig.ADMOB_BANNER_UNIT_ID.trim().isEmpty()) {
            adView.setVisibility(View.GONE);
            return;
        }
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (BuildConfig.DEBUG) Log.d("ADS_FAVS", "Banner loaded successfully");
            }
            @Override
            public void onAdFailedToLoad(LoadAdError error) {
                if (BuildConfig.DEBUG) Log.e("ADS_FAVS", "Banner failed: " + error.getCode() + " - " + error.getMessage());
            }
        });
        adView.loadAd(new AdRequest.Builder().build());
    }
}

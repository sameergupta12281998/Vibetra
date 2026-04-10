package com.vibetra.app.ui.playlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vibetra.app.R;
import com.vibetra.app.adapters.TrackAdapter;
import com.vibetra.app.data.model.Track;
import com.vibetra.app.utils.AdManager;
import com.vibetra.app.utils.PlayerLauncher;
import com.vibetra.app.viewmodel.PlaylistViewModel;

import java.util.List;

public class PlaylistTracksFragment extends Fragment {

    private static final String ARG_PLAYLIST_ID = "playlist_id";
    private static final String ARG_PLAYLIST_NAME = "playlist_name";

    private PlaylistViewModel viewModel;
    private TrackAdapter tracksAdapter;
    private long playlistId;
    private String playlistName;

    public static PlaylistTracksFragment newInstance(long playlistId, String playlistName) {
        PlaylistTracksFragment fragment = new PlaylistTracksFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PLAYLIST_ID, playlistId);
        args.putString(ARG_PLAYLIST_NAME, playlistName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playlist_tracks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            playlistId = getArguments().getLong(ARG_PLAYLIST_ID, -1L);
            playlistName = getArguments().getString(ARG_PLAYLIST_NAME, getString(R.string.playlist_tracks));
        }

        viewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        AdManager adManager = new AdManager(requireContext());

        RecyclerView rvPlaylistTracks = view.findViewById(R.id.rvPlaylistTracks);
        TextView tvEmptyTracks = view.findViewById(R.id.tvPlaylistEmpty);

        tracksAdapter = new TrackAdapter(new TrackAdapter.Listener() {
            @Override
            public void onPlay(Track track, int position) {
                List<Track> tracks = tracksAdapter.getItems();
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
                viewModel.removeTrackFromSelectedPlaylist(track);
                Toast.makeText(requireContext(), R.string.removed_from_playlist, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAddToPlaylist(Track track) {
                viewModel.addTrackToSelectedPlaylist(track);
            }
        }, true, false, true, true);

        rvPlaylistTracks.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvPlaylistTracks.setAdapter(tracksAdapter);

        viewModel.selectPlaylist(playlistId);
        viewModel.getSelectedPlaylistTracks().observe(getViewLifecycleOwner(), tracks -> {
            tracksAdapter.submitList(tracks);
            tvEmptyTracks.setVisibility(tracks == null || tracks.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(playlistName);
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationIcon(null);
        toolbar.setNavigationOnClickListener(null);
    }
}

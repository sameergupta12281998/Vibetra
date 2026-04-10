package com.vibetra.app.ui.playlist;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vibetra.app.BuildConfig;
import com.vibetra.app.R;
import com.vibetra.app.adapters.PlaylistAdapter;
import com.vibetra.app.data.model.PlaylistSummary;
import com.vibetra.app.viewmodel.PlaylistViewModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import android.util.Log;

public class PlaylistFragment extends Fragment {

    private PlaylistViewModel viewModel;
    private PlaylistAdapter playlistAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);

        EditText etPlaylistName = view.findViewById(R.id.etPlaylistName);
        Button btnCreatePlaylist = view.findViewById(R.id.btnCreatePlaylist);
        RecyclerView rvPlaylists = view.findViewById(R.id.rvPlaylists);

        playlistAdapter = new PlaylistAdapter(new PlaylistAdapter.Listener() {
            @Override
            public void onPlaylistSelected(PlaylistSummary playlist) {
                PlaylistFragment.this.onPlaylistSelected(playlist);
            }

            @Override
            public void onPlaylistDelete(PlaylistSummary playlist) {
                showDeletePlaylistDialog(playlist);
            }
        });

        rvPlaylists.setLayoutManager(new LinearLayoutManager(requireContext()));

        rvPlaylists.setAdapter(playlistAdapter);
        setupBanner(view);
        setupSwipeToDelete(rvPlaylists);

        btnCreatePlaylist.setOnClickListener(v -> {
            String name = etPlaylistName.getText() != null ? etPlaylistName.getText().toString().trim() : "";
            if (!TextUtils.isEmpty(name)) {
                viewModel.createPlaylist(name);
                etPlaylistName.setText("");
            }
        });

        viewModel.getPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            playlistAdapter.submitList(playlists);
            if (playlists != null && !playlists.isEmpty() && (viewModel.getSelectedPlaylistId().getValue() == null
                    || viewModel.getSelectedPlaylistId().getValue() <= 0)) {
                viewModel.selectPlaylist(playlists.get(0).getId());
            }
        });

        viewModel.getSelectedPlaylistId().observe(getViewLifecycleOwner(), playlistAdapter::setSelectedId);
    }

    private void onPlaylistSelected(PlaylistSummary playlist) {
        viewModel.selectPlaylist(playlist.getId());
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, PlaylistTracksFragment.newInstance(playlist.getId(), playlist.getName()))
                .addToBackStack("playlist_tracks")
                .commit();
    }

    private void showDeletePlaylistDialog(PlaylistSummary playlist) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_playlist)
                .setMessage(getString(R.string.delete_playlist_message, playlist.getName()))
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    viewModel.deletePlaylist(playlist.getId());
                    Toast.makeText(requireContext(), R.string.playlist_deleted, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void setupSwipeToDelete(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                PlaylistSummary playlist = playlistAdapter.getItemAt(position);
                if (playlist != null) {
                    showDeletePlaylistDialog(playlist);
                }
                playlistAdapter.notifyItemChanged(position);
            }
        };
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
    }

    private void setupBanner(View view) {
        AdView adView = view.findViewById(R.id.adViewPlaylist);
        if (BuildConfig.ADMOB_BANNER_UNIT_ID == null || BuildConfig.ADMOB_BANNER_UNIT_ID.trim().isEmpty()) {
            adView.setVisibility(View.GONE);
            return;
        }
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (BuildConfig.DEBUG) Log.d("ADS_PLAYLIST", "Banner loaded successfully");
            }
            @Override
            public void onAdFailedToLoad(LoadAdError error) {
                if (BuildConfig.DEBUG) Log.e("ADS_PLAYLIST", "Banner failed: " + error.getCode() + " - " + error.getMessage());
            }
        });
        adView.loadAd(new AdRequest.Builder().build());
    }
}

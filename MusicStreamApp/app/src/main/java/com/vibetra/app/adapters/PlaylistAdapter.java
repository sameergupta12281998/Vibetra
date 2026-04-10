package com.vibetra.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vibetra.app.R;
import com.vibetra.app.data.model.PlaylistSummary;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    public interface Listener {
        void onPlaylistSelected(PlaylistSummary playlist);

        void onPlaylistDelete(PlaylistSummary playlist);
    }

    private final List<PlaylistSummary> items = new ArrayList<>();
    private final Listener listener;
    private long selectedId = -1;

    public PlaylistAdapter(Listener listener) {
        this.listener = listener;
    }

    public void submitList(List<PlaylistSummary> playlists) {
        items.clear();
        if (playlists != null) {
            items.addAll(playlists);
        }
        notifyDataSetChanged();
    }

    public void setSelectedId(long selectedId) {
        this.selectedId = selectedId;
        notifyDataSetChanged();
    }

    public PlaylistSummary getItemAt(int position) {
        if (position < 0 || position >= items.size()) {
            return null;
        }
        return items.get(position);
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        PlaylistSummary playlist = items.get(position);
        holder.name.setText(playlist.getName());
        holder.count.setText(holder.itemView.getContext().getString(R.string.playlist_song_count, playlist.getTrackCount()));

        String artworkUrl = playlist.getFirstArtworkUrl();
        if (artworkUrl == null || artworkUrl.trim().isEmpty()) {
            holder.cover.setImageDrawable(null);
            holder.fallback.setVisibility(View.VISIBLE);
        } else {
            holder.fallback.setVisibility(View.GONE);
            Glide.with(holder.itemView)
                    .load(artworkUrl)
                    .into(holder.cover);
        }

        holder.itemView.setSelected(playlist.getId() == selectedId);
        holder.itemView.setOnClickListener(v -> listener.onPlaylistSelected(playlist));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onPlaylistDelete(playlist);
            return true;
        });
        holder.delete.setOnClickListener(v -> listener.onPlaylistDelete(playlist));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        ImageView fallback;
        TextView name;
        TextView count;
        ImageButton delete;

        PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.ivPlaylistCover);
            fallback = itemView.findViewById(R.id.ivPlaylistFallback);
            name = itemView.findViewById(R.id.tvPlaylistName);
            count = itemView.findViewById(R.id.tvPlaylistCount);
            delete = itemView.findViewById(R.id.btnDeletePlaylist);
        }
    }
}

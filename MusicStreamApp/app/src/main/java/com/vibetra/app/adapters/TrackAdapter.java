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
import com.vibetra.app.data.model.Track;
import com.vibetra.app.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    public interface Listener {
        void onPlay(Track track, int position);

        void onFavorite(Track track);

        void onAddToPlaylist(Track track);
    }

    private final List<Track> items = new ArrayList<>();
    private final Listener listener;
    private final boolean showFavorite;
    private final boolean showAddToPlaylist;
    private final boolean useCompactRow;
    private final boolean useDeleteIconForFavorite;

    public TrackAdapter(Listener listener, boolean showFavorite, boolean showAddToPlaylist) {
        this(listener, showFavorite, showAddToPlaylist, false);
    }

    public TrackAdapter(Listener listener, boolean showFavorite, boolean showAddToPlaylist, boolean useCompactRow) {
        this(listener, showFavorite, showAddToPlaylist, useCompactRow, false);
    }

    public TrackAdapter(Listener listener, boolean showFavorite, boolean showAddToPlaylist, boolean useCompactRow,
                        boolean useDeleteIconForFavorite) {
        this.listener = listener;
        this.showFavorite = showFavorite;
        this.showAddToPlaylist = showAddToPlaylist;
        this.useCompactRow = useCompactRow;
        this.useDeleteIconForFavorite = useDeleteIconForFavorite;
    }

    public void submitList(List<Track> tracks) {
        items.clear();
        if (tracks != null) {
            items.addAll(tracks);
        }
        notifyDataSetChanged();
    }

    public List<Track> getItems() {
        return new ArrayList<>(items);
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = useCompactRow ? R.layout.item_track_row : R.layout.item_track;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        Track track = items.get(position);
        holder.title.setText(track.getTitle());
        holder.artist.setText(track.getArtistName());
        holder.duration.setText(TimeUtils.formatDuration(track.getDurationSec()));

        Glide.with(holder.itemView)
                .load(track.getArtworkUrl())
                .placeholder(R.drawable.ic_music_note)
                .error(R.drawable.ic_music_note)
                .into(holder.thumbnail);

        holder.favorite.setVisibility(showFavorite ? View.VISIBLE : View.GONE);
        holder.addToPlaylist.setVisibility(showAddToPlaylist ? View.VISIBLE : View.GONE);
        if (holder.favorite != null) {
            holder.favorite.setImageResource(useDeleteIconForFavorite ? R.drawable.ic_close : R.drawable.ic_favorite);
        }

        holder.itemView.setOnClickListener(v -> listener.onPlay(track, holder.getBindingAdapterPosition()));
        if (holder.play != null) {
            holder.play.setOnClickListener(v -> listener.onPlay(track, holder.getBindingAdapterPosition()));
        }
        holder.favorite.setOnClickListener(v -> listener.onFavorite(track));
        holder.addToPlaylist.setOnClickListener(v -> listener.onAddToPlaylist(track));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class TrackViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView artist;
        TextView duration;
        ImageButton play;
        ImageButton favorite;
        ImageButton addToPlaylist;

        TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.ivThumbnail);
            title = itemView.findViewById(R.id.tvTrackTitle);
            artist = itemView.findViewById(R.id.tvTrackArtist);
            duration = itemView.findViewById(R.id.tvTrackDuration);
            play = itemView.findViewById(R.id.btnPlay);
            favorite = itemView.findViewById(R.id.btnFavorite);
            addToPlaylist = itemView.findViewById(R.id.btnAddToPlaylist);
        }
    }
}

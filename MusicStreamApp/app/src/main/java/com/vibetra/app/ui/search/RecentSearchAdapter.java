package com.vibetra.app.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vibetra.app.R;

import java.util.List;

public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.ViewHolder> {

    private List<String> searches;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String search);
    }

    public RecentSearchAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<String> items) {
        this.searches = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String search = searches.get(position);
        holder.tvSearch.setText(search);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(search));
        holder.btnRemove.setOnClickListener(v -> {
            if (listener instanceof InteractionListener) {
                ((InteractionListener) listener).onRemoveClick(search);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searches == null ? 0 : searches.size();
    }

    public interface InteractionListener extends OnItemClickListener {
        void onRemoveClick(String search);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSearch;
        ImageButton btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSearch = itemView.findViewById(R.id.tvSearch);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}

package com.vibetra.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vibetra.app.R;

import java.util.ArrayList;
import java.util.List;

public class SectionedRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_HORIZONTAL_LIST = 1;
    private static final int TYPE_VERTICAL_LIST = 2;
    private static final int TYPE_GRID = 3;

    private List<SectionItem> items;
    private final ItemCallback<T> callback;

    public interface ItemCallback<T> {
        void onItemClick(T item, int position);
    }

    private class SectionItem {
        Section<?> section;
        int startIndex;
        int endIndex;

        SectionItem(Section<?> section, int startIndex, int endIndex) {
            this.section = section;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }
    }

    public SectionedRecyclerViewAdapter(ItemCallback<T> callback) {
        this.callback = callback;
        this.items = new ArrayList<>();
    }

    public void setData(List<Section<?>> sections) {
        items.clear();
        for (Section<?> section : sections) {
            items.add(new SectionItem(section, items.size(), items.size() + section.getItems().size()));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        for (SectionItem item : items) {
            if (position >= item.startIndex && position <= item.endIndex) {
                if (position == item.startIndex && item.section.getViewType() != Section.TYPE_HEADER) {
                    return TYPE_HEADER;
                }
                return item.section.getViewType();
            }
        }
        return TYPE_VERTICAL_LIST;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section_header, parent, false);
                return new HeaderViewHolder(headerView);

            case TYPE_HORIZONTAL_LIST:
                View horizontalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_list, parent, false);
                return new HorizontalListViewHolder(horizontalView);

            case TYPE_GRID:
                View gridView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_list, parent, false);
                return new GridListViewHolder(gridView);

            case TYPE_VERTICAL_LIST:
            default:
                View verticalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vertical_list, parent, false);
                return new VerticalListViewHolder(verticalView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        for (SectionItem sectionItem : items) {
            if (position == sectionItem.startIndex && sectionItem.section.getViewType() != Section.TYPE_HEADER) {
                ((HeaderViewHolder) holder).bind(sectionItem.section.getTitle());
                return;
            }
            if (position > sectionItem.startIndex && position <= sectionItem.endIndex) {
                int itemIndex = position - sectionItem.startIndex - 1;
                List<?> sectionItems = sectionItem.section.getItems();
                if (itemIndex < sectionItems.size()) {
                    switch (holder.getItemViewType()) {
                        case TYPE_HORIZONTAL_LIST:
                            ((HorizontalListViewHolder) holder).bind(sectionItems, itemIndex);
                            break;
                        case TYPE_GRID:
                            ((GridListViewHolder) holder).bind(sectionItems, itemIndex);
                            break;
                        case TYPE_VERTICAL_LIST:
                            ((VerticalListViewHolder) holder).bind(sectionItems, itemIndex);
                            break;
                    }
                }
                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (SectionItem item : items) {
            count += 1 + item.section.getItems().size();
        }
        return count;
    }

    // ViewHolder for section headers
    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvSectionTitle);
        }

        void bind(String title) {
            tvTitle.setText(title);
        }
    }

    // ViewHolder for horizontal scrolling lists
    private class HorizontalListViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerView rvHorizontal;

        HorizontalListViewHolder(@NonNull View itemView) {
            super(itemView);
            rvHorizontal = itemView.findViewById(R.id.rvHorizontalCarousel);
            rvHorizontal.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }

        void bind(List<?> items, int position) {
            SimpleItemAdapter adapter = new SimpleItemAdapter(items, callback);
            rvHorizontal.setAdapter(adapter);
        }
    }

    // ViewHolder for vertical lists
    private class VerticalListViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerView rvVertical;

        VerticalListViewHolder(@NonNull View itemView) {
            super(itemView);
            rvVertical = itemView.findViewById(R.id.rvVerticalList);
            rvVertical.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }

        void bind(List<?> items, int position) {
            SimpleItemAdapter adapter = new SimpleItemAdapter(items, callback);
            rvVertical.setAdapter(adapter);
        }
    }

    // ViewHolder for grid layouts
    private class GridListViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerView rvGrid;

        GridListViewHolder(@NonNull View itemView) {
            super(itemView);
            rvGrid = itemView.findViewById(R.id.rvGrid);
            rvGrid.setLayoutManager(new GridLayoutManager(itemView.getContext(), 2));
        }

        void bind(List<?> items, int position) {
            SimpleItemAdapter adapter = new SimpleItemAdapter(items, callback);
            rvGrid.setAdapter(adapter);
        }
    }

    // Simple adapter for items within sections
    private class SimpleItemAdapter extends RecyclerView.Adapter<SimpleItemViewHolder> {
        private final List<?> items;
        private final ItemCallback<T> callback;

        SimpleItemAdapter(List<?> items, ItemCallback<T> callback) {
            this.items = items;
            this.callback = callback;
        }

        @NonNull
        @Override
        public SimpleItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_content, parent, false);
            return new SimpleItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SimpleItemViewHolder holder, int position) {
            holder.bind(items.get(position), callback);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private static class SimpleItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvContent;

        SimpleItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
        }

        <T> void bind(Object item, ItemCallback<T> callback) {
            if (item instanceof String) {
                tvContent.setText((String) item);
            } else {
                tvContent.setText(item.toString());
            }
            itemView.setOnClickListener(v -> {
                if (callback != null) {
                    callback.onItemClick((T) item, getAdapterPosition());
                }
            });
        }
    }
}

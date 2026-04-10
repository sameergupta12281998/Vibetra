package com.vibetra.app.adapters;

import java.util.List;

public class Section<T> {
    private final String title;
    private final List<T> items;
    private final int viewType;

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_VERTICAL_LIST = 1;
    public static final int TYPE_HORIZONTAL_LIST = 2;
    public static final int TYPE_GRID = 3;

    public Section(String title, List<T> items, int viewType) {
        this.title = title;
        this.items = items;
        this.viewType = viewType;
    }

    public String getTitle() {
        return title;
    }

    public List<T> getItems() {
        return items;
    }

    public int getViewType() {
        return viewType;
    }

    public boolean isHeader() {
        return viewType == TYPE_HEADER;
    }
}

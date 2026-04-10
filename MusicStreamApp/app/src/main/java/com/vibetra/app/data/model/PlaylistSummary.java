package com.vibetra.app.data.model;

public class PlaylistSummary {
    private long id;
    private String name;
    private long createdAt;
    private int trackCount;
    private String firstArtworkUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public String getFirstArtworkUrl() {
        return firstArtworkUrl;
    }

    public void setFirstArtworkUrl(String firstArtworkUrl) {
        this.firstArtworkUrl = firstArtworkUrl;
    }
}

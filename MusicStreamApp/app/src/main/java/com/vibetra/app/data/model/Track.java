package com.vibetra.app.data.model;

import java.io.Serializable;
import java.util.Objects;

public class Track implements Serializable {
    private String id;
    private String title;
    private String artistName;
    private String artworkUrl;
    private String streamUrl;
    private long durationSec;
    private String source;

    public Track() {
    }

    public Track(String id, String title, String artistName, String artworkUrl, String streamUrl, long durationSec, String source) {
        this.id = id;
        this.title = title;
        this.artistName = artistName;
        this.artworkUrl = artworkUrl;
        this.streamUrl = streamUrl;
        this.durationSec = durationSec;
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public long getDurationSec() {
        return durationSec;
    }

    public String getSource() {
        return source;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setArtworkUrl(String artworkUrl) {
        this.artworkUrl = artworkUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public void setDurationSec(long durationSec) {
        this.durationSec = durationSec;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return Objects.equals(id, track.id) && Objects.equals(source, track.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source);
    }
}

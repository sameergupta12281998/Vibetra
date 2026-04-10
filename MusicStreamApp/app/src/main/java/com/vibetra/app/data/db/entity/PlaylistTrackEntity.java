package com.vibetra.app.data.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(
        tableName = "playlist_tracks",
        primaryKeys = {"playlistId", "trackKey"},
        indices = {@Index(value = {"playlistId"})}
)
public class PlaylistTrackEntity {

    private long playlistId;

    @NonNull
    private String trackKey;

    private String trackId;
    private String source;
    private String title;
    private String artistName;
    private String artworkUrl;
    private String streamUrl;
    private long durationSec;

    public long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(long playlistId) {
        this.playlistId = playlistId;
    }

    @NonNull
    public String getTrackKey() {
        return trackKey;
    }

    public void setTrackKey(@NonNull String trackKey) {
        this.trackKey = trackKey;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        this.artworkUrl = artworkUrl;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public long getDurationSec() {
        return durationSec;
    }

    public void setDurationSec(long durationSec) {
        this.durationSec = durationSec;
    }
}

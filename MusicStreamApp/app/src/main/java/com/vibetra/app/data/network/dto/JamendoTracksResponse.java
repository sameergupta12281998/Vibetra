package com.vibetra.app.data.network.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class JamendoTracksResponse implements Serializable {
    @SerializedName("results")
    private List<JamendoTrackDto> results;

    public List<JamendoTrackDto> getResults() {
        return results;
    }

    public void setResults(List<JamendoTrackDto> results) {
        this.results = results;
    }

    public static class JamendoTrackDto implements Serializable {
        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("artist_name")
        private String artistName;

        @SerializedName("image")
        private String image;

        @SerializedName("audio")
        private String audio;

        @SerializedName("duration")
        private long duration;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getArtistName() {
            return artistName;
        }

        public String getImage() {
            return image;
        }

        public String getAudio() {
            return audio;
        }

        public long getDuration() {
            return duration;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setArtistName(String artistName) {
            this.artistName = artistName;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }
    }
}

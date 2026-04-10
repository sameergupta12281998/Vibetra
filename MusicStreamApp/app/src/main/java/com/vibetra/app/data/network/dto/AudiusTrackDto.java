package com.vibetra.app.data.network.dto;

import com.google.gson.annotations.SerializedName;

public class AudiusTrackDto {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("duration")
    private long durationSec;

    @SerializedName("user")
    private UserDto user;

    @SerializedName("artwork")
    private ArtworkDto artwork;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getDurationSec() {
        return durationSec;
    }

    public UserDto getUser() {
        return user;
    }

    public ArtworkDto getArtwork() {
        return artwork;
    }

    public static class UserDto {
        @SerializedName("name")
        private String name;

        public String getName() {
            return name;
        }
    }

    public static class ArtworkDto {
        @SerializedName("150x150")
        private String artwork150;

        @SerializedName("480x480")
        private String artwork480;

        public String getArtwork150() {
            return artwork150;
        }

        public String getArtwork480() {
            return artwork480;
        }
    }
}

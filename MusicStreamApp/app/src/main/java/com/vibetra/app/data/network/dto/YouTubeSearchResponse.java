package com.vibetra.app.data.network.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YouTubeSearchResponse {

    @SerializedName("items")
    private List<ItemDto> items;

    public List<ItemDto> getItems() {
        return items;
    }

    public static class ItemDto {
        @SerializedName("id")
        private IdDto id;

        @SerializedName("snippet")
        private SnippetDto snippet;

        public IdDto getId() {
            return id;
        }

        public SnippetDto getSnippet() {
            return snippet;
        }
    }

    public static class IdDto {
        @SerializedName("videoId")
        private String videoId;

        public String getVideoId() {
            return videoId;
        }
    }

    public static class SnippetDto {
        @SerializedName("title")
        private String title;

        @SerializedName("channelTitle")
        private String channelTitle;

        @SerializedName("thumbnails")
        private ThumbnailSetDto thumbnails;

        public String getTitle() {
            return title;
        }

        public String getChannelTitle() {
            return channelTitle;
        }

        public ThumbnailSetDto getThumbnails() {
            return thumbnails;
        }
    }

    public static class ThumbnailSetDto {
        @SerializedName("medium")
        private ThumbnailDto medium;

        @SerializedName("default")
        private ThumbnailDto defaultThumb;

        public ThumbnailDto getMedium() {
            return medium;
        }

        public ThumbnailDto getDefaultThumb() {
            return defaultThumb;
        }
    }

    public static class ThumbnailDto {
        @SerializedName("url")
        private String url;

        public String getUrl() {
            return url;
        }
    }
}

package com.vibetra.app.data.repository;

import com.vibetra.app.data.db.entity.FavoriteTrackEntity;
import com.vibetra.app.data.db.entity.PlaylistTrackEntity;
import com.vibetra.app.data.model.Track;
import com.vibetra.app.data.network.dto.AudiusTrackDto;
import com.vibetra.app.data.network.dto.JamendoTracksResponse;
import com.vibetra.app.data.network.dto.YouTubeSearchResponse;
import com.vibetra.app.utils.Constants;

public final class TrackMapper {

    private TrackMapper() {
    }

    public static Track fromAudius(AudiusTrackDto dto) {
        Track track = new Track();
        track.setId(dto.getId());
        track.setTitle(dto.getTitle());
        track.setArtistName(dto.getUser() != null ? dto.getUser().getName() : "Unknown Artist");
        track.setArtworkUrl(dto.getArtwork() != null && dto.getArtwork().getArtwork480() != null
                ? dto.getArtwork().getArtwork480()
                : dto.getArtwork() != null ? dto.getArtwork().getArtwork150() : null);
        track.setStreamUrl("https://discoveryprovider.audius.co/v1/tracks/" + dto.getId() + "/stream?app_name=Vibetra");
        track.setDurationSec(dto.getDurationSec());
        track.setSource(Constants.TRACK_SOURCE_AUDIUS);
        return track;
    }

    public static Track fromYouTube(YouTubeSearchResponse.ItemDto dto) {
        Track track = new Track();
        String videoId = dto.getId() != null ? dto.getId().getVideoId() : "";
        track.setId(videoId);
        track.setTitle(dto.getSnippet() != null ? dto.getSnippet().getTitle() : "Unknown Track");
        track.setArtistName(dto.getSnippet() != null ? dto.getSnippet().getChannelTitle() : "Unknown Artist");
        if (dto.getSnippet() != null && dto.getSnippet().getThumbnails() != null) {
            if (dto.getSnippet().getThumbnails().getMedium() != null) {
                track.setArtworkUrl(dto.getSnippet().getThumbnails().getMedium().getUrl());
            } else if (dto.getSnippet().getThumbnails().getDefaultThumb() != null) {
                track.setArtworkUrl(dto.getSnippet().getThumbnails().getDefaultThumb().getUrl());
            }
        }
        track.setStreamUrl(null);
        track.setDurationSec(0);
        track.setSource(Constants.TRACK_SOURCE_YOUTUBE);
        return track;
    }

    public static Track fromJamendo(JamendoTracksResponse.JamendoTrackDto dto) {
        Track track = new Track();
        track.setId(String.valueOf(dto.getId()));
        track.setTitle(dto.getName());
        track.setArtistName(dto.getArtistName() != null ? dto.getArtistName() : "Unknown Artist");
        track.setArtworkUrl(dto.getImage());
        track.setStreamUrl(dto.getAudio());
        track.setDurationSec(dto.getDuration());
        track.setSource(Constants.TRACK_SOURCE_JAMENDO);
        return track;
    }

    public static FavoriteTrackEntity toFavoriteEntity(Track track) {
        FavoriteTrackEntity entity = new FavoriteTrackEntity();
        entity.setTrackKey(trackKey(track));
        entity.setTrackId(track.getId());
        entity.setSource(track.getSource());
        entity.setTitle(track.getTitle());
        entity.setArtistName(track.getArtistName());
        entity.setArtworkUrl(track.getArtworkUrl());
        entity.setStreamUrl(track.getStreamUrl());
        entity.setDurationSec(track.getDurationSec());
        return entity;
    }

    public static Track fromFavoriteEntity(FavoriteTrackEntity entity) {
        return new Track(
                entity.getTrackId(),
                entity.getTitle(),
                entity.getArtistName(),
                entity.getArtworkUrl(),
                entity.getStreamUrl(),
                entity.getDurationSec(),
                entity.getSource()
        );
    }

    public static PlaylistTrackEntity toPlaylistTrack(long playlistId, Track track) {
        PlaylistTrackEntity entity = new PlaylistTrackEntity();
        entity.setPlaylistId(playlistId);
        entity.setTrackKey(trackKey(track));
        entity.setTrackId(track.getId());
        entity.setSource(track.getSource());
        entity.setTitle(track.getTitle());
        entity.setArtistName(track.getArtistName());
        entity.setArtworkUrl(track.getArtworkUrl());
        entity.setStreamUrl(track.getStreamUrl());
        entity.setDurationSec(track.getDurationSec());
        return entity;
    }

    public static Track fromPlaylistTrackEntity(PlaylistTrackEntity entity) {
        return new Track(
                entity.getTrackId(),
                entity.getTitle(),
                entity.getArtistName(),
                entity.getArtworkUrl(),
                entity.getStreamUrl(),
                entity.getDurationSec(),
                entity.getSource()
        );
    }

    public static String trackKey(Track track) {
        return track.getSource() + ":" + track.getId();
    }
}

package com.vibetra.app.data.repository;

import android.text.TextUtils;

import com.vibetra.app.BuildConfig;
import com.vibetra.app.data.model.Track;
import com.vibetra.app.data.network.AudiusApiService;
import com.vibetra.app.data.network.RetrofitClient;
import com.vibetra.app.data.network.JamendoApiService;
import com.vibetra.app.data.network.YouTubeApiService;
import com.vibetra.app.data.network.dto.AudiusTrackDto;
import com.vibetra.app.data.network.dto.AudiusTracksResponse;
import com.vibetra.app.data.network.dto.JamendoTracksResponse;
import com.vibetra.app.data.network.dto.YouTubeSearchResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicRepository {

    private static final String APP_NAME = "MusicStreamApp";

    private final AudiusApiService audiusApiService;
    private final YouTubeApiService youTubeApiService;
    private final JamendoApiService jamendoApiService;

    public MusicRepository() {
        this.audiusApiService = RetrofitClient.getAudiusApiService();
        this.youTubeApiService = RetrofitClient.getYouTubeApiService();
        this.jamendoApiService = RetrofitClient.getJamendoApiService();
    }

    public void getTrendingTracks(int limit, RepositoryCallback<List<Track>> callback) {
        audiusApiService.getTrendingTracks(limit, APP_NAME).enqueue(new Callback<AudiusTracksResponse>() {
            @Override
            public void onResponse(Call<AudiusTracksResponse> call, Response<AudiusTracksResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Track> audius = mapAudius(response.body().getData());
                    fetchJamendoTrending(limit, audius, callback);
                } else {
                    callback.onError("Failed to load trending tracks");
                }
            }

            @Override
            public void onFailure(Call<AudiusTracksResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    private void fetchJamendoTrending(int limit, List<Track> audiusResults, RepositoryCallback<List<Track>> callback) {
        jamendoApiService.getTrendingTracks("json", Math.min(limit, 25)).enqueue(new Callback<JamendoTracksResponse>() {
            @Override
            public void onResponse(Call<JamendoTracksResponse> call, Response<JamendoTracksResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Track> jamendo = mapJamendo(response.body().getResults());
                    audiusResults.addAll(jamendo);
                }
                callback.onSuccess(audiusResults);
            }

            @Override
            public void onFailure(Call<JamendoTracksResponse> call, Throwable t) {
                callback.onSuccess(audiusResults);
            }
        });
    }

    public void getPopularTracks(int limit, RepositoryCallback<List<Track>> callback) {
        getTrendingTracks(limit, callback);
    }

    public void getAlbums(int limit, RepositoryCallback<List<Track>> callback) {
        audiusApiService.searchTracks("album", limit, APP_NAME).enqueue(new Callback<AudiusTracksResponse>() {
            @Override
            public void onResponse(Call<AudiusTracksResponse> call, Response<AudiusTracksResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(mapAudius(response.body().getData()));
                } else {
                    callback.onError("Failed to load albums");
                }
            }

            @Override
            public void onFailure(Call<AudiusTracksResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void searchTracks(String query, int limit, RepositoryCallback<List<Track>> callback) {
        audiusApiService.searchTracks(query, limit, APP_NAME).enqueue(new Callback<AudiusTracksResponse>() {
            @Override
            public void onResponse(Call<AudiusTracksResponse> call, Response<AudiusTracksResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null
                        && !response.body().getData().isEmpty()) {
                    List<Track> results = mapAudius(response.body().getData());
                    fetchJamendoSearch(query, limit, results, callback);
                } else {
                    fallbackToJamendoSearch(query, limit, callback);
                }
            }

            @Override
            public void onFailure(Call<AudiusTracksResponse> call, Throwable t) {
                fallbackToJamendoSearch(query, limit, callback);
            }
        });
    }

    private void fetchJamendoSearch(String query, int limit, List<Track> audiusResults, RepositoryCallback<List<Track>> callback) {
        jamendoApiService.searchTracks("json", query, Math.min(limit, 25)).enqueue(new Callback<JamendoTracksResponse>() {
            @Override
            public void onResponse(Call<JamendoTracksResponse> call, Response<JamendoTracksResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResults() != null) {
                    List<Track> jamendo = mapJamendo(response.body().getResults());
                    audiusResults.addAll(jamendo);
                }
                if (!audiusResults.isEmpty()) {
                    callback.onSuccess(audiusResults);
                } else {
                    fallbackToYouTube(query, limit, callback);
                }
            }

            @Override
            public void onFailure(Call<JamendoTracksResponse> call, Throwable t) {
                if (!audiusResults.isEmpty()) {
                    callback.onSuccess(audiusResults);
                } else {
                    fallbackToYouTube(query, limit, callback);
                }
            }
        });
    }

    private void fallbackToJamendoSearch(String query, int limit, RepositoryCallback<List<Track>> callback) {
        jamendoApiService.searchTracks("json", query, Math.min(limit, 25)).enqueue(new Callback<JamendoTracksResponse>() {
            @Override
            public void onResponse(Call<JamendoTracksResponse> call, Response<JamendoTracksResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResults() != null) {
                    List<Track> results = mapJamendo(response.body().getResults());
                    if (!results.isEmpty()) {
                        callback.onSuccess(results);
                        return;
                    }
                }
                fallbackToYouTube(query, limit, callback);
            }

            @Override
            public void onFailure(Call<JamendoTracksResponse> call, Throwable t) {
                fallbackToYouTube(query, limit, callback);
            }
        });
    }

    private void fallbackToYouTube(String query, int limit, RepositoryCallback<List<Track>> callback) {
        if (TextUtils.isEmpty(BuildConfig.YOUTUBE_API_KEY)) {
            callback.onSuccess(Collections.emptyList());
            return;
        }
        youTubeApiService.searchVideos("snippet", limit, query, "video", BuildConfig.YOUTUBE_API_KEY)
                .enqueue(new Callback<YouTubeSearchResponse>() {
                    @Override
                    public void onResponse(Call<YouTubeSearchResponse> call, Response<YouTubeSearchResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getItems() != null) {
                            List<Track> list = new ArrayList<>();
                            for (YouTubeSearchResponse.ItemDto dto : response.body().getItems()) {
                                list.add(TrackMapper.fromYouTube(dto));
                            }
                            callback.onSuccess(list);
                        } else {
                            callback.onError("No results found");
                        }
                    }

                    @Override
                    public void onFailure(Call<YouTubeSearchResponse> call, Throwable t) {
                        callback.onError("Search failed: " + t.getMessage());
                    }
                });
    }

    private List<Track> mapAudius(List<AudiusTrackDto> data) {
        List<Track> list = new ArrayList<>();
        if (data == null) {
            return list;
        }
        for (AudiusTrackDto dto : data) {
            list.add(TrackMapper.fromAudius(dto));
        }
        return list;
    }

    private List<Track> mapJamendo(List<JamendoTracksResponse.JamendoTrackDto> data) {
        List<Track> list = new ArrayList<>();
        if (data == null) {
            return list;
        }
        for (JamendoTracksResponse.JamendoTrackDto dto : data) {
            list.add(TrackMapper.fromJamendo(dto));
        }
        return list;
    }
}

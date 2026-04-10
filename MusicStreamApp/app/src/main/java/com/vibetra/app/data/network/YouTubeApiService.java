package com.vibetra.app.data.network;

import com.vibetra.app.data.network.dto.YouTubeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YouTubeApiService {

    @GET("youtube/v3/search")
    Call<YouTubeSearchResponse> searchVideos(
            @Query("part") String part,
            @Query("maxResults") int maxResults,
            @Query("q") String query,
            @Query("type") String type,
            @Query("key") String apiKey
    );
}

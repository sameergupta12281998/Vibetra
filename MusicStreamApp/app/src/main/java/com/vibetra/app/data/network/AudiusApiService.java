package com.vibetra.app.data.network;

import com.vibetra.app.data.network.dto.AudiusTracksResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AudiusApiService {

    @GET("v1/tracks/trending")
    Call<AudiusTracksResponse> getTrendingTracks(@Query("limit") int limit, @Query("app_name") String appName);

    @GET("v1/tracks/search")
    Call<AudiusTracksResponse> searchTracks(@Query("query") String query, @Query("limit") int limit, @Query("app_name") String appName);
}

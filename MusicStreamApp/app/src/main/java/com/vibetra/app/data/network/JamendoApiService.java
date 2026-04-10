package com.vibetra.app.data.network;

import com.vibetra.app.data.network.dto.JamendoTracksResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JamendoApiService {

    @GET("tracks/")
    Call<JamendoTracksResponse> getTrendingTracks(@Query("format") String format, @Query("limit") int limit);

    @GET("tracks/")
    Call<JamendoTracksResponse> searchTracks(@Query("format") String format, @Query("search") String query, @Query("limit") int limit);

    @GET("tracks/")
    Call<JamendoTracksResponse> getRandomTracks(@Query("format") String format, @Query("limit") int limit, @Query("order") String order);
}

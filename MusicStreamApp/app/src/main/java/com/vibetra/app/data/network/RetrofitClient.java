package com.vibetra.app.data.network;

import com.vibetra.app.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public final class RetrofitClient {

    private static final String AUDIUS_BASE_URL = "https://api.audius.co/";
    private static final String YOUTUBE_BASE_URL = "https://www.googleapis.com/";
    private static final String JAMENDO_BASE_URL = "https://api.jamendo.com/v3.0/";

    private static AudiusApiService audiusApiService;
    private static YouTubeApiService youTubeApiService;
    private static JamendoApiService jamendoApiService;

    private RetrofitClient() {
    }

    public static AudiusApiService getAudiusApiService() {
        if (audiusApiService == null) {
            audiusApiService = buildRetrofit(AUDIUS_BASE_URL).create(AudiusApiService.class);
        }
        return audiusApiService;
    }

    public static YouTubeApiService getYouTubeApiService() {
        if (youTubeApiService == null) {
            youTubeApiService = buildRetrofit(YOUTUBE_BASE_URL).create(YouTubeApiService.class);
        }
        return youTubeApiService;
    }

    public static JamendoApiService getJamendoApiService() {
        if (jamendoApiService == null) {
            jamendoApiService = buildRetrofit(JAMENDO_BASE_URL).create(JamendoApiService.class);
        }
        return jamendoApiService;
    }

    private static Retrofit buildRetrofit(String baseUrl) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BASIC : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

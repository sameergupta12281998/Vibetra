package com.vibetra.app.data.network.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AudiusTracksResponse {

    @SerializedName("data")
    private List<AudiusTrackDto> data;

    public List<AudiusTrackDto> getData() {
        return data;
    }
}

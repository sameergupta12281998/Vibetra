package com.vibetra.app.utils;

public final class Constants {

    public static final String TRACK_SOURCE_AUDIUS = "audius";
    public static final String TRACK_SOURCE_YOUTUBE = "youtube";
    public static final String TRACK_SOURCE_JAMENDO = "jamendo";

    public static final String PREFS_NAME = "music_stream_prefs";
    public static final String PREF_PLAY_COUNT = "pref_play_count";
    public static final String PREF_HD_UNLOCKED_UNTIL = "pref_hd_unlocked_until";

    public static final String ACTION_PLAY_TRACKS = "com.vibetra.app.action.PLAY_TRACKS";
    public static final String EXTRA_TRACK_LIST = "extra_track_list";
    public static final String EXTRA_TRACK_INDEX = "extra_track_index";
    public static final String EXTRA_IS_HD = "extra_is_hd";

    public static final String NOTIFICATION_CHANNEL_ID = "music_playback_channel";
    public static final int NOTIFICATION_ID = 1001;

    private Constants() {
    }
}
